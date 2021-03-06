package ru.dartilla.examinator.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.dartilla.examinator.domain.Excercise;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserInterfaceTest {

    @Mock
    private InOut inOut;

    private UserInterface userInterface;
    private ByteArrayOutputStream outByteArrayStream;

    @BeforeEach
    private void setUp() {
        outByteArrayStream = new ByteArrayOutputStream(1000);
        userInterface = new UserInterface(inOut);
    }

    @Test
    public void printQuestion() {
        when(inOut.getOut()).thenReturn(new PrintStream(outByteArrayStream));
        Excercise excercise = new Excercise("Собака это?", null,
                Stream.of("Рыба", "Животное", "Гриб").collect(Collectors.toList()));
        userInterface.printQuestion(excercise);
        String outResult = new String(outByteArrayStream.toByteArray(), StandardCharsets.UTF_8);
        assertThat(outResult).contains(excercise.getQuestion());
        assertThat(outResult).contains(String.join(", ", excercise.getAnswersToChoose()));
    }

    @Test
    public void readAnswer() {
        Set<String> answers = Stream.of("firstAnswer", "secondAnswer").collect(Collectors.toSet());
        when(inOut.getIn()).thenReturn(new Scanner(new ByteArrayInputStream(String.join(", ", answers).getBytes())));
        assertThat(userInterface.readAnswer().containsAll(answers)).isTrue();
    }

    @Test
    public void readAnswerWrongDelimiter() {
        Set<String> answers = Stream.of("firstAnswer", "secondAnswer").collect(Collectors.toSet());
        when(inOut.getIn()).thenReturn(new Scanner(new ByteArrayInputStream(String.join(",", answers).getBytes())));
        assertThat(userInterface.readAnswer().containsAll(answers)).isFalse();
    }
}