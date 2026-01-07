public class CustomException {
    public static void validateAge(int age) throws InvalideExceptions {
        if (age < 18) {
            throw new InvalideExceptions("Age must be 18 or above to vote.");
        }
        System.out.println("Valid age for voting.");
    }

    public static void main(String[] args) {
        try {
            validateAge(18);
        } catch (InvalideExceptions e) {
            System.out.println("Caught Exception: " + e.getMessage());
        }

    }
}
class InvalideExceptions extends Exception{
    public InvalideExceptions(String message){
        super(message);
    }
}
