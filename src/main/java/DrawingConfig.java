import java.nio.file.Path;

public class DrawingConfig {
    private Path inputPath;
    private Path outputPath;
    private String separator;
    private double textHeight;
    private boolean doPrintId;
    private boolean doPrintCoords;
    private boolean doPrintCode;
    private boolean doPrintHeight;
    private boolean is3D;

    public Path getInputPath() {
        return inputPath;
    }

    public Path getOutputPath() {
        return outputPath;
    }

    public String getSeparator() {
        return separator;
    }

    public double getTextHeight() {
        return textHeight;
    }

    public boolean doPrintId() {
        return doPrintId;
    }

    public boolean doPrintCoords() {
        return doPrintCoords;
    }

    public boolean doPrintCode() {
        return doPrintCode;
    }

    public boolean doPrintHeight() {
        return doPrintHeight;
    }

    public boolean isIs3D() {
        return is3D;
    }

    private DrawingConfig(Path inputPath, Path outputPath, String separator, double textHeight, boolean doPrintId, boolean doPrintCoords, boolean doPrintCode, boolean doPrintHeight, boolean is3D) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
        this.separator = separator;
        this.textHeight = textHeight;
        this.doPrintId = doPrintId;
        this.doPrintCoords = doPrintCoords;
        this.doPrintCode = doPrintCode;
        this.doPrintHeight = doPrintHeight;
        this.is3D = is3D;
    }

    public static DrawingConfigBuilder builder(){
        return new DrawingConfigBuilder();
    }

    public static class DrawingConfigBuilder {
        private Path inputPath;
        private Path outputPath;
        private String separator;
        private double textHeight;
        private boolean doPrintId;
        private boolean doPrintCoords;
        private boolean doPrintCode;
        private boolean doPrintHeight;
        private boolean is3D;



        public DrawingConfigBuilder setInputPath(Path inputPath) {
            this.inputPath = inputPath;
            return this;
        }

        public DrawingConfigBuilder setOutputPath(Path outputPath) {
            this.outputPath = outputPath;
            return this;
        }

        public DrawingConfigBuilder setSeparator(String separator) {
            this.separator = separator;
            return this;
        }

        public DrawingConfigBuilder setTextHeight(double textHeight) {
            this.textHeight = textHeight;
            return this;
        }

        public DrawingConfigBuilder setDoPrintId(boolean doPrintId) {
            this.doPrintId = doPrintId;
            return this;
        }

        public DrawingConfigBuilder setDoPrintCoords(boolean doPrintCoords) {
            this.doPrintCoords = doPrintCoords;
            return this;
        }

        public DrawingConfigBuilder setDoPrintCode(boolean doPrintCode) {
            this.doPrintCode = doPrintCode;
            return this;
        }

        public DrawingConfigBuilder setDoPrintHeight(boolean doPrintHeight) {
            this.doPrintHeight = doPrintHeight;
            return this;
        }

        public DrawingConfigBuilder setIs3D(boolean is3D) {
            this.is3D = is3D;
            return this;
        }

        public DrawingConfig build() {
            return new DrawingConfig(inputPath, outputPath, separator, textHeight, doPrintId, doPrintCoords, doPrintCode, doPrintHeight, is3D);
        }
    }
}
