{ pkgs }: {
  deps = [
    pkgs.jdk21      # JVM runtime + Gradle wrapper
    pkgs.nodejs_20  # Vaadin production frontend build (vite/rollup)
  ];
}
