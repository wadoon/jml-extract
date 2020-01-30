public class Test {
    public <T> T id(T t) {
        return t;
    }

    public void call() {
        // Here the Javac can infer `obj : String`
        var obj = this.<Object>id("I am a string.");
    }
}