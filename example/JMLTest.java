package test;

/**
 * @author Alexander Weigl
 * @version 1 (1/24/20)
 */
public class JMLTest {
    //@ invariant true
    //@ && false;

    public void blocky() {
        /*@ behavior
            requires true; ensures true;
         */
        {
            int i = 0;
        }


        /*@ loop_invariant  true;         */
        for (int i = 0; i < 0; i++) {
            //@ set a = 2;
        }
    }

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
