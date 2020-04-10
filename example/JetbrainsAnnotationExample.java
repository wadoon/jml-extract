public class JetbrainsAnnotationExample {
    @org.jetbrains.annotations.Nullable
    public String nullableString;
    @org.jetbrains.annotations.NotNull
    public String notNullString;

    @org.jetbrains.annotations.Nullable
    Object func() {
        return null;
    }
}