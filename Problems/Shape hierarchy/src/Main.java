abstract class Shape {

    abstract double getPerimeter();

    abstract double getArea();
}

class Triangle extends Shape {
    private double a;
    private double b;
    private double c;
    private double s;
    Triangle (double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.s = (a + b + c) / 2;
    }
    @Override
    double getPerimeter() {
        return a + b + c;
    }

    @Override
    double getArea() {
        return Math.sqrt(s * (s - a) * (s - b) * (s - c));
    }
}

class Rectangle extends Shape {

    private double a;
    private double b;

    Rectangle(double a, double b) {
        this.a = a;
        this.b = b;
    }

    @Override
    double getPerimeter() {
        return 2 * (a + b);
    }

    @Override
    double getArea() {
        return a * b;
    }
}

class Circle extends Shape {
    private double r;

    Circle(double r) {
        this.r = r;
    }

    @Override
    double getPerimeter() {
        return 2 * Math.PI * r;
    }

    @Override
    double getArea() {
        return Math.PI * r * r;
    }
}