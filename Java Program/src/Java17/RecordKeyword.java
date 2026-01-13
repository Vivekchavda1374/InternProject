public class RecordKeyword {
    public record Employee(String name, int age) {
        public Employee {
            if (age < 18) {
                throw new IllegalArgumentException("Age must be 18 or above");
            }
        }
    }
    public static void main(String[] args) {
        Employee emp = new Employee("Alice", 30);  // ✅ Works fine
//        Employee emp2 = new Employee("Bob", 16);   // ❌ Throws IllegalArgumentException
        String text = """
        Line 1
            Line 2
        Line 3
        """.stripIndent();

        System.out.println(text);
        String json = """
    {
      "name": "Alice",
      "age": 25,
      "city": "New York"
    }
    """;
        System.out.println(json);
    }
}
