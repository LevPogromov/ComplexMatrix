public class Complex {
    private final double valid;
    private final double im;
    public Complex(double valid, double im) {
        this.valid = valid;
        this.im = im;
    }
    public double get_real(){
        return valid;
    }
    public double get_im(){
        return im;
    }
    public Complex sum(Complex another) {
        return new Complex(this.valid + another.valid, this.im + another.im);
    }
    public Complex minus(Complex another) {
        return new Complex(this.valid - another.valid, this.im - another.im);
    }
    public Complex multi(Complex another) {
        return new Complex(this.valid * another.valid - this.im * another.im,
                this.valid * another.im + this.im * another.valid);
    }
    public Complex div(Complex another) {
        if (another.valid == 0 && another.im == 0) {
            throw new ArithmeticException("Second complex number is zero ");
        }
        double new_valid = (this.valid * another.valid + this.im * another.im)/
                (another.valid * another.valid + another.im * another.im);
        double new_im = (another.valid * this.im - this.valid * another.im)/
                (another.valid * another.valid + another.im * another.im);
        return new Complex(new_valid, new_im);
    }
    public String to_string() {
        if (im < 0) {
            if (im == (int)im && valid == (int)valid) {
                return (int)valid + "" + (int)im + "i";
            }
            else if (im == (int)im){
                return valid + "" + (int)im + "i";
            }
            else if (valid == (int)im){
                return (int)valid + "" + im + "i";
            }
            else {
                return valid + "" + im + "i";
            }
        }
        else {
            if (valid == (int)valid && im == (int)im) {
                return (int)valid + "+" + (int)im + "i";
            }
            else if (valid == (int)valid){
                return (int)valid + "+" + im + "i";
            }
            else if (im == (int)im){
                return valid + "+" + (int)im + "i";
            }
            else {
                return valid + "+" + im + "i";
            }
        }
    }
}
