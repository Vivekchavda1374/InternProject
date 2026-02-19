package Exceptions;

public class Main  {
//    public static void divide(int a,int b ) {
//        try {
//            if(b==0){
//                System.out.println("divide by zero not possible");
//            }
//        } catch (ArithmeticException e) {
//            System.out.println("Cannot divide by zero!");
//        }
//
//    }
    public static void main(String[] args) {
//            try {
//                int[] numbers = {1, 2, 3};
//                int result = 10 / 0;
//                System.out.println(numbers[5]);
//            }
//            catch (ArithmeticException e) {
//                System.out.println("Error: Cannot divide by zero.");
//            } catch (ArrayIndexOutOfBoundsException e) {
//                System.out.println("Error: Array index is out of bounds.");
//            } catch (Exception e) {
//                System.out.println("A general exception occurred: " + e.getMessage());
//            }
//            catch (ArithmeticException | ArrayIndexOutOfBoundsException  e ){System.out.println("A exception occurred: " + e.getMessage());}
//        finally { System.out.println("Execution of code done");

        try{
            int[] arr ={1,2,3,4,5,6};
            try{
                int d = 10/0;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            System.out.println(arr[6]);
        }catch (Exception e ){
            System.out.println(e.fillInStackTrace());
        }
    }

}