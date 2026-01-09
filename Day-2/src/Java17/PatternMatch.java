public class PatternMatch {
    static void process(Object obj) {
        switch (obj) {
            case Integer i -> System.out.println("Integer: " + (i * 2));
            case String s -> System.out.println("String: " + s.toUpperCase());
            case Double d -> System.out.println("Double: " + (d + 10.5));
            case null -> System.out.println("Null value provided!");
            default -> System.out.println("Unknown type!");
        }
    }

    public static void main(String[] args) {
        process(10);         // Integer: 20
        process("hello");    // String: HELLO
        process(5.5);        // Double: 16.0
        process(null);       // Null value provided!
    }
}