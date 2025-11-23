import SourceParser.Lexer.Lexer;
import SourceParser.Model.MethodCall;
import SourceParser.MethodCallTracker.MethodCallTracker;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

public class MethodCallTrackerTest {

    @Test
    @DisplayName("메서드 호출 추적 - 객체.메서드() 패턴")
    public void testTrackObjectMethodCall() {
        String code = """
            {
                userService.getUsers();
            }
            """;

        Lexer lexer = new Lexer(code);
        MethodCallTracker tracker = new MethodCallTracker(lexer);

        List<MethodCall> calls = tracker.trackMethodCalls();

        assertNotNull(calls);
        assertEquals(1, calls.size());
        assertEquals("userService", calls.get(0).getTargetClass());
        assertEquals("getUsers", calls.get(0).getTargetMethod());
    }

    @Test
    @DisplayName("메서드 호출 추적 - this.메서드() 패턴")
    public void testTrackThisMethodCall() {
        String code = """
            {
                this.validate();
            }
            """;

        Lexer lexer = new Lexer(code);
        MethodCallTracker tracker = new MethodCallTracker(lexer);

        List<MethodCall> calls = tracker.trackMethodCalls();

        assertNotNull(calls);
        assertEquals(1, calls.size());
        assertEquals("this", calls.get(0).getTargetClass());
        assertEquals("validate", calls.get(0).getTargetMethod());
    }

    @Test
    @DisplayName("메서드 호출 추적 - 같은 클래스 내 호출")
    public void testTrackSameClassMethodCall() {
        String code = """
            {
                logAccess();
            }
            """;

        Lexer lexer = new Lexer(code);
        MethodCallTracker tracker = new MethodCallTracker(lexer);

        List<MethodCall> calls = tracker.trackMethodCalls();

        assertNotNull(calls);
        assertEquals(1, calls.size());
        assertNull(calls.get(0).getTargetClass());
        assertEquals("logAccess", calls.get(0).getTargetMethod());
    }

    @Test
    @DisplayName("메서드 호출 추적 - 여러 호출")
    public void testTrackMultipleMethodCalls() {
        String code = """
            {
                userService.getUsers();
                this.validate();
                logAccess();
                repository.save();
            }
            """;

        Lexer lexer = new Lexer(code);
        MethodCallTracker tracker = new MethodCallTracker(lexer);

        List<MethodCall> calls = tracker.trackMethodCalls();

        assertNotNull(calls);
        assertEquals(4, calls.size());
    }

    @Test
    @DisplayName("메서드 호출 추적 - 중첩 블록")
    public void testTrackNestedBlocks() {
        String code = """
            {
                if (true) {
                    service.process();
                }
            }
            """;

        Lexer lexer = new Lexer(code);
        MethodCallTracker tracker = new MethodCallTracker(lexer);

        List<MethodCall> calls = tracker.trackMethodCalls();

        assertNotNull(calls);
        assertEquals(1, calls.size());
        assertEquals("service", calls.get(0).getTargetClass());
        assertEquals("process", calls.get(0).getTargetMethod());
    }

    @Test
    @DisplayName("메서드 호출 추적 - 빈 본문")
    public void testTrackEmptyBody() {
        String code = "{}";

        Lexer lexer = new Lexer(code);
        MethodCallTracker tracker = new MethodCallTracker(lexer);

        List<MethodCall> calls = tracker.trackMethodCalls();

        assertNotNull(calls);
        assertEquals(0, calls.size());
    }
}