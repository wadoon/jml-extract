package test;

/**
 * @author Alexander Weigl
 * @version 1 (1/24/20)
 */
public class JMLTest {
    //@ invariant true && false;

    public void boxi() {
        Integer i = 0;
    }

    public void blocky() {
        /*@ behavior
            requires true;
            ensures true;
         */
        {
            int i = 0;
        }


        /*@ loop_invariant  true;         */
        for (int i = 0; i < 0; i++) {
            //@ set a = 2;
        }
    }
}
