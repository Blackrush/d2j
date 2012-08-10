package org.d2j.common.random;

import java.util.Random;
import java.util.concurrent.atomic.AtomicReference;

/**
 * User: Blackrush
 * Date: 11/12/11
 * Time: 19:54
 * IDE : IntelliJ IDEA
 */
public class Dice {
    private static AtomicReference<Random> RANDOM = new AtomicReference<>(new Random(System.currentTimeMillis()));

    public static final Dice EMPTY_DICE = new Dice(0, 0, 0);

    public static Dice parseDice(String str){
        int i = str.indexOf('d'),
            j = str.indexOf('+');

        int round = Integer.parseInt(str.substring(0, i)),
            num   = Integer.parseInt(j > 0 ? str.substring(i + 1, j) : str.substring(i + 1)),
            add   = j > 0 ? Integer.parseInt(str.substring(j + 1)) : 0;

        return new Dice(round, num, add);
    }

    private int round, num, add;

    public Dice() {

    }

    public Dice(int round, int num, int add) {
        this.round = round;
        this.num = num;
        this.add = add;
    }

    public Dice(Dice dice){
        this.round = dice.round;
        this.num   = dice.num;
        this.add   = dice.add;
    }

    public int roll(){

        int result = 0;

        for (int i = 0; i < round; ++i){
            result += RANDOM.get().nextInt(num);
        }

        return result + add;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getAdd() {
        return add;
    }

    public void setAdd(int add) {
        this.add = add;
    }

    public Dice addAdd(int add) {
        this.add += add;
        return this;
    }

    public int min(){
        return round + add;
    }

    public int max(){
        return round * num + add;
    }

    public boolean isConst(){
        return num <= 1 || round <= 0;
    }

    @Override
    public String toString() {
        return round + "d" + num + (add > 0 ? "+" + add : "");
    }
}
