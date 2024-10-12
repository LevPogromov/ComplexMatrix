public class Matrix {
    private final Complex[][] matrix;
    private final int rows;
    private final int columns;

    public Matrix(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        matrix = new Complex[rows][columns];
    }

    //for filling with examples
    public void filling(Complex[] values) throws MatrixException {
        int cnt = 0;
        if (values.length != rows * columns) {
            throw new MatrixException("Wrong sizes for filling, length of list should" +
                    " be equal to rows*columns. Please, change it to continue \n");
        }
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] = values[cnt];
                cnt++;
            }
        }
    }

    //to perform operations inside the matrix
    //it is up to the users to try and catch if they want to use a setter or getter
    public void set(int row, int column, Complex value) {
        if (row >= this.rows || column >= this.columns || row < 0 || column < 0){
            throw new IndexOutOfBoundsException("Wrong pointers to get");
        }
        matrix[row][column] = value;
    }

    public Complex get(int row, int column) {
        if (row >= this.rows || column >= this.columns){
            throw new IndexOutOfBoundsException("Wrong pointers to get");
        }
        return matrix[row][column];
    }

    public Matrix summing(Matrix another) throws MatrixException {
        if (this.rows != another.rows || this.columns != another.columns) {
            throw new MatrixException("Sizes should be equal for summing \n");
        }
        Matrix summed = new Matrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                summed.set(i, j, this.get(i, j).sum(another.get(i, j)));
            }
        }
        return summed;
    }

    public Matrix subtract(Matrix another) throws MatrixException {
        if (this.rows != another.rows || this.columns != another.columns) {
            throw new MatrixException("Wrong sizes for subtraction \n");
        }
        Matrix subtracted = new Matrix(rows, columns);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                subtracted.set(i, j, this.get(i, j).minus(another.get(i, j)));
            }
        }
        return subtracted;
    }

    //for O(n^(2.81)) complexity with huge constant(just for interest)
    public Matrix multiply_strassen(Matrix another) throws MatrixException {
        if (this.columns != another.rows) {
            throw new MatrixException("The number of columns of the 1-st matrix" +
                    " should be equal to the number of rows of the 2-nd for multiplying \n");
        }
        int max_size = Math.max(Math.max(this.rows, this.columns),
                Math.max(another.rows, another.columns));
        int necessary_size = 1;
        while (necessary_size < max_size) {
            necessary_size *= 2;
        }
        Matrix A = new Matrix(necessary_size, necessary_size);
        Matrix B = new Matrix(necessary_size, necessary_size);
        for (int i = 0; i < necessary_size; i++){
            for (int j = 0; j < necessary_size; j++){
                A.set(i,j, new Complex(0,0));
                B.set(i,j, new Complex(0,0));
            }
        }
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < this.columns; j++) {
                A.set(i, j, this.get(i, j));
            }
        }
        for (int i = 0; i < another.rows; i++) {
            for (int j = 0; j < another.columns; j++) {
                B.set(i, j, another.get(i, j));
            }
        }
        return strassen_alg(A, B);
    }

    private Matrix strassen_alg(Matrix A, Matrix B) throws MatrixException{
        if (A.rows == 1 || B.rows == 1) {
            Matrix res = new Matrix(1, 1);
            res.set(0, 0, A.get(0, 0).multi(B.get(0, 0)));
            return res;
        }
        int size_of_sub = A.rows / 2;
        Matrix A11 = new Matrix(size_of_sub, size_of_sub);
        Matrix A21 = new Matrix(size_of_sub, size_of_sub);
        Matrix A12 = new Matrix(size_of_sub, size_of_sub);
        Matrix A22 = new Matrix(size_of_sub, size_of_sub);
        Matrix B11 = new Matrix(size_of_sub, size_of_sub);
        Matrix B21 = new Matrix(size_of_sub, size_of_sub);
        Matrix B12 = new Matrix(size_of_sub, size_of_sub);
        Matrix B22 = new Matrix(size_of_sub, size_of_sub);
        // filling submatrices
        for (int i = 0; i < size_of_sub; i++) {
            for (int j = 0; j < size_of_sub; j++) {
                A11.set(i, j, A.get(i, j));
                A21.set(i, j, A.get(i + size_of_sub, j));
                A12.set(i, j, A.get(i, j + size_of_sub));
                A22.set(i, j, A.get(i + size_of_sub, j + size_of_sub));
                B11.set(i, j, B.get(i, j));
                B21.set(i, j, B.get(i + size_of_sub, j));
                B12.set(i, j, B.get(i, j + size_of_sub));
                B22.set(i, j, B.get(i + size_of_sub, j + size_of_sub));
            }
        }
        // 7 multiplications
        Matrix result = new Matrix(A.rows, A.rows);
        Matrix D = strassen_alg(A11.summing(A22), B11.summing(B22));
        Matrix D1 = strassen_alg(A12.subtract(A22), B21.summing(B22));
        Matrix D2 = strassen_alg(A21.subtract(A11), B11.summing(B12));
        Matrix H1 = strassen_alg(A11.summing(A12), B22);
        Matrix H2 = strassen_alg(A21.summing(A22), B11);
        Matrix V1 = strassen_alg(A22, B21.subtract(B11));
        Matrix V2 = strassen_alg(A11, B12.subtract(B22));
        Matrix C11 = D.summing(D1).summing(V1).subtract(H1);
        Matrix C12 = V2.summing(H1);
        Matrix C21 = V1.summing(H2);
        Matrix C22 = D.summing(D2).summing(V2).subtract(H2);
        // filling ready matrix
        for (int i = 0; i < size_of_sub; i++) {
            for (int j = 0; j < size_of_sub; j++) {
                result.set(i, j, C11.get(i, j));
                result.set(i, j + size_of_sub, C12.get(i, j));
                result.set(i + size_of_sub, j, C21.get(i, j));
                result.set(i + size_of_sub, j + size_of_sub, C22.get(i, j));
            }
        }
        return result;
    }

    public Matrix multiply(Matrix another) throws MatrixException {
        if (this.columns != another.rows) {
            throw new MatrixException("The number of columns of the 1-st matrix" +
                    " should be equal to the number of rows of the 2-nd for multiplying \n");
        }
        Matrix res = new Matrix(this.rows, another.columns);
        for (int i = 0; i < this.rows; i++) {
            for (int j = 0; j < another.columns; j++) {
                Complex summa = new Complex(0, 0);
                for (int k = 0; k < this.columns; k++) {
                    summa = summa.sum(this.get(i, k).multi(another.get(k, j)));
                }
                res.set(i, j, summa);
            }
        }
        return res;
    }

    public Complex determinant() throws MatrixException{
        if (this.rows != this.columns) {
            throw new MatrixException("The number of rows and columns should be equal" +
                    " to find a determinant \n");
        }
        if (this.rows == 1) {
            return this.get(0, 0);
        }
        if (this.rows == 2) {
            return this.get(0, 0).multi(this.get(1, 1)).
                    minus(this.get(0, 1).multi(this.get(1, 0)));
        }
        Complex det = new Complex(0, 0);
        for (int i = 0; i < this.rows; i++) {
            Matrix submatrix = new Matrix(this.rows - 1, this.rows - 1);
            for (int j = 1; j < this.rows; j++) {
                for (int k = 0; k < this.columns; k++) {
                    if (k < i) {
                        submatrix.set(j - 1, k, this.get(j, k));
                    } else if (k > i) {
                        submatrix.set(j - 1, k - 1, this.get(j, k));
                    }
                }
            }
            Complex mini_det = submatrix.determinant();
            if (i % 2 == 0) {
                det = det.sum(this.get(0, i).multi(mini_det));
            }
            else {
                det = det.minus(this.get(0, i).multi(mini_det));
            }
        }
        return det;
    }

    public Matrix inverse() throws MatrixException, DetException{
        if (this.rows != this.columns) {
            throw new MatrixException("The number of rows and columns should be equal" +
                    " to inverse \n");
        }
        Complex det = this.determinant();
        if (det.get_real() == 0 && det.get_im() == 0) {
            throw new DetException("Determinant = 0, can't find inverse matrix \n");
        }
        Matrix complement = new Matrix(this.rows, this.columns);
        for (int i = 0; i < this.rows; i++){
            for (int j = 0; j < this.columns; j++){
                Matrix submatrix = new Matrix(this.rows - 1, this.columns - 1);
                int subi = 0;
                int subj = 0;
                for (int k = 0; k < this.rows; k++){
                    for (int q = 0; q < this.columns; q++){
                        if (k == i || q == j) {
                            continue;
                        }
                        submatrix.set(subi, subj, this.get(k, q));
                        subj++;
                        if (subj == this.columns - 1) {
                            subj = 0;
                            subi++;
                        }
                    }
                }

                Complex subdet = submatrix.determinant();
                if ((i + j) % 2 == 0){
                    complement.set(i, j, subdet);
                }
                else{
                    complement.set(i,j, subdet.multi(new Complex(-1, 0)));
                }
            }
        }
        Matrix adj = complement.transpose();
        Matrix inverse = new Matrix(this.rows, this.columns);
        for (int i = 0; i < this.rows; i++){
            for (int j = 0; j < this.columns; j++){
                inverse.set(i, j, adj.get(i, j).multi(new Complex(1, 0).div(det)));
            }
        }
        return inverse;
    }

    public Matrix division(Matrix another) throws MatrixException, DetException {
        return this.multiply(another.inverse());
    }

    public Matrix transpose() {
        Matrix res = new Matrix(columns, rows);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                res.set(j, i, this.get(i, j));
            }
        }
        return res;
    }

    public void show() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(matrix[i][j].to_string() + "\t");
            }
            System.out.println();
        }
    }
}