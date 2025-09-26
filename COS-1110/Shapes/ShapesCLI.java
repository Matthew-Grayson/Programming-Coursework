/**
 * ShapesCLI: a small CLI to create 2D/3D shapes and compute basic properties.
 *
 * Usage:
 *   - Choose 2D or 3D
 *   - Choose a shape
 *   - Enter positive dimensions (units are arbitrary but consistent)
 *
 * Design:
 *   - Shapes are registered via ShapeSpec in a Registry; menus build dynamically.
 *   - Two abstract bases model 2D vs 3D capabilities.
 *
 * Assumptions:
 *   - All dimensions > 0.
 *   - Console I/O via java.util.Scanner.
 *
 * References:
 *   Gibbs, M. (2021, October 20) Practical Application for Java: Creating & Using Java Classes. Study.com.
 *     https://study.com/academy/lesson/practical-application-for-java-creating-using-java-classes.html
 *   Zandbergen, P. (2024, June 20) Object-Oriented Programming: Objects, Classes & Methods. Study.com.
 *     https://study.com/academy/lesson/oop-object-oriented-programming-objects-classes-interfaces.html
 *   Oracle. (n.d.). java.util (Java Platform SE 8). In Java Platform, Standard Edition 8 API Specification.
 *     Retrieved September 26, 2025, from https://docs.oracle.com/javase/8/docs/api/java/util/package-summary.html
 *   Oracle. (n.d.). Function<T,R> (Java Platform SE 8). In Java Platform, Standard Edition 8 API Specification.
 *     Retrieved September 26, 2025, from https://docs.oracle.com/javase/8/docs/api/java/util/function/Function.html
 */

import static java.lang.Math.PI;
import java.util.*;
import java.util.function.Function;

public class ShapesCLI {

    public static abstract class Shapes {
        /**
         * Abstract base class for all shapes.
         *
         * Provides methods to retrieve the shape's
         * dimensions, name, and type.
         */
        public abstract String getDimensions();
        public abstract String getName();
        public abstract String getType();
    }

    public static abstract class TwoDimensionalShapes extends Shapes {
        /**
         * Abstract class for 2D shapes.
         *
         * Extends the Shapes class and adds methods for
         * calculating area and perimeter.
         */
        public abstract double calculateArea();
        public abstract double calculatePerimeter();
    }

    public static abstract class ThreeDimensionalShapes extends Shapes {
        /**
         * Abstract class for 3D shapes.
         *
         * Extends the Shapes class and adds methods for
         * calculating surface area and volume.
         */
        public abstract double calculateSurfaceArea();
        public abstract double calculateVolume();
    }

    public static class Rectangle extends TwoDimensionalShapes {
        private double length;
        private double width;

        public Rectangle(double length, double width) {
            this.length = length;
            this.width = width;
        }

        @Override
        public double calculateArea() {
            return length * width;
        }

        @Override
        public double calculatePerimeter() {
            return 2 * (length + width);
        }

        @Override
        public String getDimensions() {
            return "Length: " + length + ", Width: " + width;
        }

        @Override
        public String getName() {
            return "Rectangle";
        }

        @Override
        public String getType() {
            return "2D";
        }

    }

    public static class Cube extends ThreeDimensionalShapes {
        private double side;

        public Cube(double side) {
            this.side = side;
        }

        @Override
        public double calculateSurfaceArea() {
            return 6 * side * side;
        }

        @Override
        public double calculateVolume() {
            return side * side * side;
        }

        @Override
        public String getDimensions() {
            return "Side: " + side;
        }

        @Override
        public String getName() {
            return "Cube";
        }

        @Override
        public String getType() {
            return "3D";
        }
    }

    public static class Cylinder extends ThreeDimensionalShapes {
        private double radius;
        private double height;

        public Cylinder(double radius, double height) {
            this.radius = radius;
            this.height = height;
        }

        @Override
        public double calculateSurfaceArea() {
            return 2 * PI * radius * (radius + height);
        }

        @Override
        public double calculateVolume() {
            return PI * radius * radius * height;
        }

        @Override
        public String getDimensions() {
            return "Radius: " + radius + ", Height: " + height;
        }

        @Override
        public String getName() {
            return "Cylinder";
        }

        @Override
        public String getType() {
            return "3D";
        }
    }

    public static class Sphere extends ThreeDimensionalShapes {
        private double radius;

        public Sphere(double radius) {
            this.radius = radius;
        }

        @Override
        public double calculateSurfaceArea() {
            return 4 * PI * radius * radius;
        }

        @Override
        public double calculateVolume() {
            return (4.0 /3.0) * PI * radius * radius * radius;
        }

        @Override
        public String getDimensions() {
            return "Radius: " + radius;
        }

        @Override
        public String getName() {
            return "Sphere";
        }

        @Override
        public String getType() {
            return "3D";
        }
    }

//    Dynamic CLI Infrastructure

    /**
     * Enum representing the types of shapes: 2D or 3D.
     */
    enum ShapeType { TWO_D, THREE_D }

    static final class ShapeSpec {
        /**
         * Specifies attributes of a shape.
         *
         * Includes its name, type, required dimensions,
         * and a factory function to create instances dynamically.
         */
        final String name;
        final ShapeType type;
        final String[] dimensions;
        final Function<double[], Shapes> factory;
        ShapeSpec(String name, ShapeType type, String[] dimensions, Function<double[], Shapes> factory) {
            this.name = name; this.type = type; this.dimensions = dimensions; this.factory = factory;
        }
    }

    static final class Registry {
        /**
         * Registry for managing and retrieving shape specifications.
         *
         * Shapes are grouped by their type (2D or 3D).
         */
        private static final Map<ShapeType, List<ShapeSpec>> byType = new EnumMap<>(ShapeType.class);
        static {
            byType.put(ShapeType.TWO_D, new ArrayList<>());
            byType.put(ShapeType.THREE_D, new ArrayList<>());
        }

        // Registers a new shape specification.
        static void register(ShapeSpec spec) { byType.get(spec.type).add(spec); }

        // Retrieves the list of shape specifications for a given type.
        static List<ShapeSpec> get(ShapeType type) { return Collections.unmodifiableList(byType.get(type)); }
    }

    // Register shapes once to populate CLI
    static {
        Registry.register(new ShapeSpec(
                "Rectangle", ShapeType.TWO_D,
                new String[]{"Length", "Width"},
                dims -> new Rectangle(dims[0], dims[1])
        ));
        Registry.register(new ShapeSpec(
                "Cylinder", ShapeType.THREE_D,
                new String[]{"Radius", "Height"},
                dims -> new Cylinder(dims[0], dims[1])
        ));
        Registry.register(new ShapeSpec(
                "Cube", ShapeType.THREE_D,
                new String[]{"Side"},
                dims -> new Cube(dims[0])
        ));
        Registry.register(new ShapeSpec(
                "Sphere", ShapeType.THREE_D,
                new String[]{"Radius"},
                dims -> new Sphere(dims[0])
        ));
        // Add new shapes here to update the CLI menus dynamically
    }

    // CLI
    public static void main(String[] args) {
        /**
         * Provides a menu-driven interface for creating and interacting with shapes.
         */
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.println("\n=== Shapes CLI ===");
                System.out.println("1) 2D Shapes");
                System.out.println("2) 3D Shapes");
                System.out.println("0) Exit");
                int typeChoice = readInt(scanner, "Choose an option: ", 0, 2);
                if (typeChoice == 0) break;

                ShapeType chosenType = (typeChoice == 1) ? ShapeType.TWO_D : ShapeType.THREE_D;
                List<ShapeSpec> specs = Registry.get(chosenType);
                if (specs.isEmpty()) {
                    System.out.println("No shapes registered for " + chosenType);
                    continue;
                }

                // list shapes dynamically
                System.out.println("\nAvailable " + chosenType + " shapes:");
                for (int i = 0; i < specs.size(); i++) {
                    System.out.printf("%d) %s%n", i + 1, specs.get(i).name);
                }
                System.out.println("0) Back");
                int shapeChoice = readInt(scanner, "Choose a shape: ", 0, specs.size());
                if (shapeChoice == 0) continue;

                ShapeSpec spec = specs.get(shapeChoice - 1);
                double[] dims = new double[spec.dimensions.length];
                for (int i = 0; i < dims.length; i++) {
                    dims[i] = readDouble(scanner, spec.dimensions[i] + ": ", v -> v > 0, "Value must be > 0.");
                }

                Shapes shape = spec.factory.apply(dims);
                System.out.println("\nYou chose: " + shape.getType() + " " + shape.getName());
                System.out.println("Dimensions: " + shape.getDimensions());

                if (shape instanceof TwoDimensionalShapes s2) {
                    System.out.printf("Area: %.4f%n", s2.calculateArea());
                    System.out.printf("Perimeter: %.4f%n", s2.calculatePerimeter());
                } else if (shape instanceof ThreeDimensionalShapes s3) {
                    System.out.printf("Surface Area: %.4f%n", s3.calculateSurfaceArea());
                    System.out.printf("Volume: %.4f%n", s3.calculateVolume());
                }

                System.out.println();
            }
        }
        System.out.println("Goodbye!");
    }

    // Input helpers
    private static int readInt(Scanner scanner, String prompt, int min, int max) {
        /**
         * Reads, validates and returns user menu selection.
         *
         * @param scanner The scanner to read input from.
         * @param prompt The prompt to display to the user.
         * @param min The minimum valid value.
         * @param max The maximum valid value.
         * @return The integer entered by the user.
         */
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                int v = Integer.parseInt(line);
                if (v < min || v > max) throw new NumberFormatException();
                return v;
            } catch (NumberFormatException e) {
                System.out.printf("Enter an integer in [%d, %d].%n", min, max);
            }
        }
    }

    private static double readDouble(Scanner scanner, String prompt,
                                     java.util.function.DoublePredicate ok,
                                     String errMsg) {
        /**
         * Reads, validates and returns shape dimensions input by the user.
         *
         * @param scanner The scanner to read input from.
         * @param prompt The prompt to display to the user.
         * @param ok A predicate to validate the input.
         * @param errMsg The error message to display for invalid input.
         * @return The double entered by the user.
         */
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                double v = Double.parseDouble(line);
                if (!ok.test(v)) { System.out.println(errMsg); continue; }
                return v;
            } catch (NumberFormatException e) {
                System.out.println("Enter a number (e.g., 3.14).");
            }
        }
    }

}