package ru.otus.hw03;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.hw03.actions.Movable;
import ru.otus.hw03.actions.QueueControl;
import ru.otus.hw03.commands.Command;
import ru.otus.hw03.commands.LogCommand;
import ru.otus.hw03.commands.MoveCommand;
import ru.otus.hw03.commands.RepeatCommand;
import ru.otus.hw03.exceptions.MoveException;
import ru.otus.hw03.exceptions.handlers.MoveExceptionLog;
import ru.otus.hw03.exceptions.handlers.MoveExceptionRepeatLog;
import ru.otus.hw03.exceptions.handlers.MoveExceptionTwoRepeatLog;
import ru.otus.hw03.ioc.IoC;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DisplayName("Тестирование обработки исключений. Обработчик исключений должен:")
class ExceptionHandlingTest {
    private BlockingQueue<Command> commandQueue;
    private QueueControl queueControl;
    private QueueThread queueThread;
    private Logger logger;
    private Movable movable;
    private final List<Class<? extends Command>> processedCommands = new ArrayList<>();


    @BeforeEach
    @SuppressWarnings("unchecked")
    void setUp() throws InterruptedException {
        commandQueue = (BlockingQueue<Command>) spy(LinkedBlockingQueue.class);
        doAnswer(
                i -> {
                    Command c = (Command) i.callRealMethod();
                    processedCommands.add(c.getClass());
                    return c;
                }
        ).when(commandQueue).take();
        queueThread = new QueueThreadImpl(commandQueue);
        queueControl = new QueueControl(queueThread, commandQueue);
        logger = mock(Logger.class);
        movable = mock(Movable.class);
        when(movable.getPosition()).thenThrow(new MoveException("Can't get position"));

        IoC.<Command>resolve("IoC.Registry", "Command.Queue",
                (Function<Object[], BlockingQueue<Command>>) (args) -> commandQueue).execute();
        IoC.<Command>resolve("IoC.Registry", "Application.Logger",
                (Function<Object[], Logger>) (args) -> logger).execute();
        queueControl.start();
    }

    @Test
    @DisplayName("Cтавить команду, пишущую в лог, в очередь команд")
    void exceptionHandlerLogCommandTest() throws InterruptedException {
        IoC.<Command>resolve("IoC.Registry", "MoveCommand.MoveException",
                (Function<Object[], Command>) (args) -> new MoveExceptionLog((Command) args[0], (Exception) args[1])).execute();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        doAnswer(i -> {
            countDownLatch.countDown();
            return null;
        }).when(logger).log(any(), anyString());
        MoveCommand cmd = new MoveCommand(movable);
        commandQueue.add(cmd);
        var i = countDownLatch.await(30, TimeUnit.SECONDS);
        verify(commandQueue, atLeast(2)).take();
        assertTrue(processedCommands.containsAll(List.of(MoveCommand.class, LogCommand.class)));
        verify(movable, times(1)).getPosition();
        verify(logger, times(1)).log(Level.SEVERE, "MoveCommand error. Reason: Can't get position");
    }

    @Test
    @DisplayName("Повторить команду, и при повторной ошибке поставить команду, пишущую в лог, в очередь команд.")
    void exceptionHandlerRepeatAndLogCommandTest() throws InterruptedException {
        IoC.<Command>resolve("IoC.Registry", "MoveCommand.MoveException",
                (Function<Object[], Command>) (args) -> new MoveExceptionRepeatLog((Command) args[0], (Exception) args[1])).execute();
        IoC.<Command>resolve("IoC.Registry", "RepeatCommand.MoveException",
                (Function<Object[], Command>) (args) -> new MoveExceptionLog((Command) args[0], (Exception) args[1])).execute();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        doAnswer(i -> {
            countDownLatch.countDown();
            return null;
        }).when(logger).log(any(), anyString());
        MoveCommand cmd = new MoveCommand(movable);
        commandQueue.add(cmd);
        var i = countDownLatch.await(30, TimeUnit.SECONDS);
        verify(commandQueue, atLeast(3)).take();
        assertTrue(processedCommands.containsAll(List.of(MoveCommand.class, LogCommand.class, RepeatCommand.class)));
        verify(movable, times(2)).getPosition();
        verify(logger, times(1)).log(Level.SEVERE, "RepeatCommand error. Reason: Can't get position");
    }

    @Test
    @DisplayName("Повторить дважды команду, и поставить команду, пишущую в лог, в очередь команд.")
    void exceptionHandlerTwoRepeatAndLogCommandTest() throws InterruptedException {
        IoC.<Command>resolve("IoC.Registry", "MoveCommand.MoveException",
                (Function<Object[], Command>) (args) -> new MoveExceptionTwoRepeatLog((Command) args[0], (Exception) args[1])).execute();
        IoC.<Command>resolve("IoC.Registry", "RepeatCommand.MoveException",
                (Function<Object[], Command>) (args) -> new MoveExceptionTwoRepeatLog((Command) args[0], (Exception) args[1])).execute();
        IoC.<Command>resolve("IoC.Registry", "SecondRepeatCommand.MoveException",
                (Function<Object[], Command>) (args) -> new MoveExceptionTwoRepeatLog((Command) args[0], (Exception) args[1])).execute();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        doAnswer(i -> {
            countDownLatch.countDown();
            return null;
        }).when(logger).log(any(), anyString());
        MoveCommand cmd = new MoveCommand(movable);
        commandQueue.add(cmd);
        var i = countDownLatch.await(30, TimeUnit.SECONDS);
        verify(commandQueue, atLeast(3)).take();
        assertTrue(processedCommands.containsAll(List.of(MoveCommand.class, LogCommand.class, RepeatCommand.class)));
        verify(movable, times(3)).getPosition();
        verify(logger, times(1)).log(Level.SEVERE, "SecondRepeatCommand error. Reason: Can't get position");
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        queueControl.softStop();
        queueThread.join(30000);
        processedCommands.clear();
    }
}
