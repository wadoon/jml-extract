public class Test {
    public <T> T id(T t) {
        return t;
    }

    public void call() {
        // Here the Javac can infer `obj : String`
        Object obj = this.<String>id("I am a string.");
    }

    public void a() {
        String a = "a";
        recursive(a + "abc");
    }

    /*@ public normal_behaviour
        ensures f(u) && true && new Boolean(true) && this.a();
        requires false;
        assignable \nothing;
     */
    public <U> void recursive(U u) {
        recursive(null);
    }
}