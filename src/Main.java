public class Main {
    public static void main(String[] args) {
        Complex[] arr1 = {
                new Complex(3, 3), new Complex(1, 1),
                new Complex(2, 2), new Complex(5,5)
        };
        // (3+3i 1+1i)
        // (2+2i 5+5i) 1-st matrix
        Complex[] arr2 = {
                new Complex(1, 2), new Complex(2, 1),
                new Complex(4, 2), new Complex(5,2)
        };
        // (1+2i 2+1i)
        // (4+2i 5+2i) 2-nd matrix
        Matrix matrix1 = new Matrix(2, 2);
        Matrix matrix2 = new Matrix(2, 2);
        try {
            matrix1.filling(arr1);
            matrix2.filling(arr2);
        } catch (MatrixException e) {
            System.err.print(e.getMessage());
            return;
        }
        try {
            Matrix matrix3 = matrix1.summing(matrix2);
            System.out.print("Sum of matrices: \n");
            matrix3.show();
        } catch (MatrixException e) {
            System.err.print(e.getMessage());
        }
        try {
            Matrix matrix5 = matrix1.subtract(matrix2);
            System.out.print("Subtraction of matrices: \n");
            matrix5.show();
        } catch (MatrixException e) {
            System.err.print(e.getMessage());
        }
        try {
            Matrix matrix4 = matrix1.multiply(matrix2);
            System.out.print("Multiply of matrices: \n");
            matrix4.show();
        } catch (MatrixException e) {
            System.err.print(e.getMessage());
        }
        try {
            Matrix matrix6 = matrix1.multiply_strassen(matrix2);
            System.out.print("Multiply of matrices with Strassen algorithm \n");
            matrix6.show();
        } catch (MatrixException e) {
            System.err.print(e.getMessage());
        }
        Matrix matrix7 = matrix1.transpose();
        System.out.print("Transpose 1-st matrix: \n");
        matrix7.show();
        try {
            Complex det = matrix1.determinant();
            System.out.print("Determinant of the 1-st matrix: \n");
            System.out.print(det.to_string());
            System.out.println();
        } catch (MatrixException e) {
            System.err.print(e.getMessage());
        }
        try {
            Matrix matrix8 = matrix1.division(matrix2);
            System.out.print("Division of the 1-st and 2-nd matrices: \n");
            matrix8.show();
        } catch (MatrixException | DetException | ArithmeticException e) {
            System.err.print(e.getMessage());
        }
    }
}