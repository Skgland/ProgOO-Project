package de.webtwob.view.lighthouse;

/**
 * Created by BB20101997 on 06. Mär. 2017.
 * <p>
 * This class stores the constants that are used as patterns by the LighthouseView to display menu text and score
 */
public class Letter {

    public static final boolean[][] S = {
            {true, true, true}, {true, false, false}, {true, true, true}, {false, false, true}, {true, true, true},
            };

    public static final boolean[][] C = {
            {true, true, true}, {true, false, false}, {true, false, false}, {true, false, false}, {true, true, true}
    };
    public static final boolean[][] O = {
            {true, true, true}, {true, false, true}, {true, false, true}, {true, false, true}, {true, true, true}
    };
    public static final boolean[][] R = {
            {true, true, false}, {true, false, true}, {true, true, false}, {true, false, true}, {true, false, true}
    };
    public static final boolean[][] E = {
            {true, true, true}, {true, false, false}, {true, true, true}, {true, false, false}, {true, true, true}
    };

    public static final boolean[][] G = {
            {false, true, true}, {true, false, false}, {true, true, true}, {true, false, true}, {false, true, true}
    };
    public static final boolean[][] A = {
            {false, true, false}, {true, false, true}, {true, false, true}, {true, true, true}, {true, false, true}
    };

    public static final boolean[][] M = {
            {true, false, false, false, true},
            {true, true, false, true, true},
            {true, false, true, false, true},
            {true, false, false, false, true},
            {true, false, false, false, true}
    };

    public static final boolean[][] N = {
            {true, false, false, false, true},
            {true, true, false, false, true},
            {true, false, true, false, true},
            {true, false, false, true, true},
            {true, false, false, false, true}
    };

    public static final boolean[][] V = {
            {true, false, true}, {true, false, true}, {true, false, true}, {false, true, false}, {false, true, false}
    };

    public static final boolean[][] U = {
            {true, false, true}, {true, false, true}, {true, false, true}, {true, false, true}, {false, true, false}
    };

    public static final boolean[][] I = {
            {true, true, true}, {false, true, false}, {false, true, false}, {false, true, false}, {true, true, true}
    };

    public static final boolean[][][] SCORE = {S, C, O, R, E};
    public static final boolean[][][] GAME  = {G, A, M, E};
    public static final boolean[][][] OVER  = {O, V, E, R};
    public static final boolean[][][] MENU  = {M, E, N, U};
    public static final boolean[][][] MAIN  = {M, A, I, N};

    public static final boolean[][][] NUMBERS = {
            {
                    {false, true, false},
                    {true, false, true},
                    {true, false, true},
                    {true, false, true},
                    {false, true, false}
                    //0
            }, {
                    {false, false, true},
                    {false, true, true},
                    {true, false, true},
                    {false, false, true},
                    {false, false, true}
                    //1
            }, {
                    {false, true, false},
                    {true, false, true},
                    {false, false, true},
                    {false, true, false},
                    {true, true, true}
                    //2
            }, {
                    {true, true, false},
                    {false, false, true},
                    {false, true, false},
                    {false, false, true},
                    {true, true, false}
                    //3
            }, {
                    {true, false, true},
                    {true, false, true},
                    {true, true, true},
                    {false, false, true},
                    {false, false, true}
                    //4
            }, {
                    {true, true, true},
                    {true, false, false},
                    {true, true, false},
                    {false, false, true},
                    {true, true, false}
                    //5
            }, {
                    {false, true, true},
                    {true, false, false},
                    {true, true, false},
                    {true, false, true},
                    {false, true, false}
                    //6
            }, {
                    {true, true, true},
                    {false, false, true},
                    {false, false, true},
                    {false, false, true},
                    {false, false, true}
                    //7
            }, {
                    {false, true, false},
                    {true, false, true},
                    {false, true, false},
                    {true, false, true},
                    {false, true, false}
                    //8
            }, {
                    {false, true, false},
                    {true, false, true},
                    {false, true, true},
                    {false, false, true},
                    {true, true, false}
                    //9
            }

    };
}
