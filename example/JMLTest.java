package test;

/**
 * @author Alexander Weigl
 * @version 1 (1/24/20)
 */
public class JMLTest {
    //@ invariant a>=0 && a<= 10;
    int a = 0;

    public void boxi() {
        Integer i = 0;
    }

    /*@ public normal_behaviour
        requires a!=0 && a%2==0;
        ensures a!=0 ==> a!=2;
     */
    public void blocky() {
        /*@ behavior
            requires true;
            ensures true;
         */
        {
            int i = 0;
        }


        /*@ loop_invariant  i >= 0 ;
         */
        for (int i = 0; i < 0; i++) {
            //@ set a = 2;
        }
    }
}
