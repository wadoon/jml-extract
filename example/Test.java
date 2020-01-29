public class Test {
    public <T> T id(T t) {
        return t;
    }

    public void call() {
        // Here the Javac can infer `obj : String`
        var obj = id<Object>("I am a string.");
    }
}