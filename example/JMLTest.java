package test;

/**
 * @author Alexander Weigl
 * @version 1 (1/24/20)
 */
public class JMLTest {
    //@ invariant true
    //@ && false;

    public void a() {
        var a = "a";
        recursive(a + "abc");
    }

    /*@ public normal_behaviour
        ensures true;
        requires true;
     */
    public <U> void recursive(U u) {
        recursive(null);
    }
}
