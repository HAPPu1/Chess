package com.example.happu.chess;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import static com.example.happu.chess.R.color.colorAccent;

public class MainActivity extends AppCompatActivity {
    private static final int WHITE_PAWN = R.drawable.wp, BLACK_PAWN = R.drawable.bp;
    private static final int WHITE_ROOK = R.drawable.wr, BLACK_ROOK = R.drawable.br;
    private static final int WHITE_KNIGHT = R.drawable.wn, BLACK_KNIGHT = R.drawable.bn;
    private static final int WHITE_BISHOP = R.drawable.wb, BLACK_BISHOP = R.drawable.bb;
    private static final int WHITE_QUEEN = R.drawable.wq, BLACK_QUEEN = R.drawable.bq;
    private static final int WHITE_KING = R.drawable.wk, BLACK_KING = R.drawable.bk;
    private static boolean IS_TROOPS_SELECTED = false, WHICH_TROOPS_TURNS = false, KING_CHECK = false, CHECK_MATE = false;
    private static int PREVIOUS_I, PREVIOUS_J;
    public ImageView[][] chess_array;
    public ImageView check_event;
    private int[][] troops = new int[8][8];
    private int[][] putTroops = new int[8][8], tempPutTroops = new int[8][8];
    private int[][] troops_img = {{R.drawable.wp, R.drawable.wr, R.drawable.wn, R.drawable.wb, R.drawable.wq, R.drawable.wk},
            {R.drawable.bp, R.drawable.br, R.drawable.bn, R.drawable.bb, R.drawable.bq, R.drawable.bk}};
    private int[][] imageView_id = {{R.id.a1, R.id.b1, R.id.c1, R.id.d1, R.id.e1, R.id.f1, R.id.g1, R.id.h1},
            {R.id.a2, R.id.b2, R.id.c2, R.id.d2, R.id.e2, R.id.f2, R.id.g2, R.id.h2},
            {R.id.a3, R.id.b3, R.id.c3, R.id.d3, R.id.e3, R.id.f3, R.id.g3, R.id.h3},
            {R.id.a4, R.id.b4, R.id.c4, R.id.d4, R.id.e4, R.id.f4, R.id.g4, R.id.h4},
            {R.id.a5, R.id.b5, R.id.c5, R.id.d5, R.id.e5, R.id.f5, R.id.g5, R.id.h5},
            {R.id.a6, R.id.b6, R.id.c6, R.id.d6, R.id.e6, R.id.f6, R.id.g6, R.id.h6},
            {R.id.a7, R.id.b7, R.id.c7, R.id.d7, R.id.e7, R.id.f7, R.id.g7, R.id.h7},
            {R.id.a8, R.id.b8, R.id.c8, R.id.d8, R.id.e8, R.id.f8, R.id.g8, R.id.h8}};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        WHICH_TROOPS_TURNS = false;
        chess_array = new ImageView[8][8];
        check_event = findViewById(R.id.check_event);
        initializeChessElement();
        setInitialTroops();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                mSetOnClickListener(i, j, chess_array[i][j]);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset_btn) {
            resetChessGame();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void initializeChessElement() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                chess_array[i][j] = findViewById(imageView_id[i][j]);
            }
        }
    }

    protected void setInitialTroops() {
        for (int i = 0; i < 8; i++) {
            chess_array[1][i].setImageResource(WHITE_PAWN);
            troops[1][i] = WHITE_PAWN;
            chess_array[6][i].setImageResource(BLACK_PAWN);
            troops[6][i] = BLACK_PAWN;
        }

        for (int i = 0; i < 3; i++) {
            chess_array[0][i].setImageResource(troops_img[0][i + 1]);
            troops[0][i] = troops_img[0][i + 1];
            chess_array[0][7 - i].setImageResource(troops_img[0][i + 1]);
            troops[0][7 - i] = troops_img[0][i + 1];
            chess_array[7][i].setImageResource(troops_img[1][i + 1]);
            troops[7][i] = troops_img[1][i + 1];
            chess_array[7][7 - i].setImageResource(troops_img[1][i + 1]);
            troops[7][7 - i] = troops_img[1][i + 1];
        }

        for (int i = 0; i < 2; i++) {
            chess_array[0][3 + i].setImageResource(troops_img[0][4 + i]);
            troops[0][3 + i] = troops_img[0][i + 4];
            chess_array[7][3 + i].setImageResource(troops_img[1][4 + i]);
            troops[7][3 + i] = troops_img[1][i + 4];
        }

    }

    protected void mSetOnClickListener(final int i, final int j, ImageView v) {
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!whichTroopsTurns() && !CHECK_MATE) {
                    if (isTroop(i, j) && !isBlackTroops(i, j)) {
                        if (isSelected()) {
                            resetColor();
                            resetTempPutTroops();
                            IS_TROOPS_SELECTED = false;
                        }
                        switch (whichTroops(i, j)) {
                            case WHITE_PAWN: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                whitePawnSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }
                            case WHITE_KNIGHT: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                knightSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }
                            case WHITE_ROOK: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                rookSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }
                            case WHITE_BISHOP: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                bishopSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }
                            case WHITE_QUEEN: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                queenSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }
                            case WHITE_KING: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                kingSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }
                        }
                        IS_TROOPS_SELECTED = true;
                    } else {
                        if (isSelected()) {

                            if (tempPutTroops[i][j] == 1) {
                                KING_CHECK = false;
                                if (troops[PREVIOUS_I][PREVIOUS_J] == WHITE_PAWN && i == 7) {
                                    //Todo
                                } else {
                                    chess_array[i][j].setImageResource(troops[PREVIOUS_I][PREVIOUS_J]);
                                    troops[i][j] = troops[PREVIOUS_I][PREVIOUS_J];
                                    troops[PREVIOUS_I][PREVIOUS_J] = 0;
                                    chess_array[PREVIOUS_I][PREVIOUS_J].setImageResource(0);
                                }
                                resetTempPutTroops();
                                if (!KING_CHECK) {
                                    check_event.setImageResource(0);
                                }
                                if (setKingCheck(WHICH_TROOPS_TURNS)) {
                                    if (isCheckMate(WHICH_TROOPS_TURNS)) {
                                        CHECK_MATE = true;
                                        check_event.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                        check_event.setImageResource(R.mipmap.ic_launcher);
                                    } else {
                                        KING_CHECK = true;
                                        check_event.setImageResource(R.drawable.check);
                                    }

                                } else if (isStalemate(WHICH_TROOPS_TURNS)) {
                                    CHECK_MATE = true;
                                }
                                resetColor();
                                IS_TROOPS_SELECTED = false;
                                WHICH_TROOPS_TURNS = true;
                            } else {
                                resetColor();
                                resetTempPutTroops();
                                IS_TROOPS_SELECTED = false;
                            }
                        }
                    }
                }
                if (whichTroopsTurns() && !CHECK_MATE) {
                    if (isTroop(i, j) && isBlackTroops(i, j)) {
                        if (isSelected()) {
                            resetColor();
                            resetTempPutTroops();
                            IS_TROOPS_SELECTED = false;
                        }
                        switch (whichTroops(i, j)) {

                            case BLACK_PAWN: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                blackPawnSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }
                            case BLACK_KNIGHT: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                knightSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }

                            case BLACK_ROOK: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                rookSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }
                            case BLACK_BISHOP: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                bishopSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }
                            case BLACK_QUEEN: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                queenSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }
                            case BLACK_KING: {
                                chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                                kingSelected(i, j);
                                PREVIOUS_I = i;
                                PREVIOUS_J = j;
                                setTempPutTroops();
                                optimizeTempPutTroops(PREVIOUS_I, PREVIOUS_J);
                                setColorOnChess();
                                break;
                            }
                        }
                        IS_TROOPS_SELECTED = true;
                    } else {
                        if (isSelected()) {
                            if (tempPutTroops[i][j] == 1) {
                                KING_CHECK = false;
                                if (troops[PREVIOUS_I][PREVIOUS_J] == BLACK_PAWN && i == 0) {
                                    //Todo
                                } else {
                                    chess_array[i][j].setImageResource(troops[PREVIOUS_I][PREVIOUS_J]);
                                    troops[i][j] = troops[PREVIOUS_I][PREVIOUS_J];
                                    troops[PREVIOUS_I][PREVIOUS_J] = 0;
                                    chess_array[PREVIOUS_I][PREVIOUS_J].setImageResource(0);
                                }
                                resetTempPutTroops();
                                if (!KING_CHECK) {
                                    check_event.setImageResource(0);
                                }
                                if (setKingCheck(WHICH_TROOPS_TURNS)) {
                                    if (isCheckMate(WHICH_TROOPS_TURNS)) {
                                        CHECK_MATE = true;
                                        check_event.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                                        check_event.setImageResource(R.mipmap.ic_launcher);
                                    } else {
                                        KING_CHECK = true;
                                        check_event.setImageResource(R.drawable.check);
                                    }
                                } else if (isStalemate(WHICH_TROOPS_TURNS)) {
                                    CHECK_MATE = true;
                                }
                                resetColor();
                                IS_TROOPS_SELECTED = false;
                                WHICH_TROOPS_TURNS = false;
                            } else {
                                resetColor();
                                resetTempPutTroops();
                                IS_TROOPS_SELECTED = false;
                            }
                        }
                    }
                }
            }
        });
    }

    protected boolean isTroop(final int i, final int j) {
        return chess_array[i][j].getDrawable() != null;
    }

    protected int whichTroops(final int i, final int j) {
        if (troops[i][j] == WHITE_PAWN) {
            return WHITE_PAWN;
        } else if (troops[i][j] == BLACK_PAWN) {
            return BLACK_PAWN;
        } else if (troops[i][j] == WHITE_ROOK) {
            return WHITE_ROOK;
        } else if (troops[i][j] == BLACK_ROOK) {
            return BLACK_ROOK;
        } else if (troops[i][j] == WHITE_BISHOP) {
            return WHITE_BISHOP;
        } else if (troops[i][j] == BLACK_BISHOP) {
            return BLACK_BISHOP;
        } else if (troops[i][j] == WHITE_KNIGHT) {
            return WHITE_KNIGHT;
        } else if (troops[i][j] == BLACK_KNIGHT) {
            return BLACK_KNIGHT;
        } else if (troops[i][j] == WHITE_QUEEN) {
            return WHITE_QUEEN;
        } else if (troops[i][j] == BLACK_QUEEN) {
            return BLACK_QUEEN;
        } else if (troops[i][j] == WHITE_KING) {
            return WHITE_KING;
        } else if (troops[i][j] == BLACK_KING) {
            return BLACK_KING;
        } else {
            return 0;
        }
    }

    protected boolean isBlackTroops(final int i, final int j) {
        for (int k = 0; k < 6; ++k) {
            if (troops[i][j] == troops_img[1][k]) {
                return true;
            }
        }
        return false;
    }

    protected boolean isSelected() {
        return IS_TROOPS_SELECTED;
    }

    protected void setColorOnChess() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (tempPutTroops[i][j] == 1) {
                    chess_array[i][j].setBackgroundColor(getResources().getColor(R.color.select_troops_color));
                }
            }
        }
    }

    protected void resetColor() {
        for (int i = 0; i < 8; ++i) {
            if (i % 2 == 0) {
                for (int j = 0; j < 8; ) {
                    chess_array[i][j++].setBackgroundColor(getResources().getColor(R.color.black));
                    chess_array[i][j++].setBackgroundColor(getResources().getColor(R.color.white));
                }
            } else {
                for (int j = 0; j < 8; ) {
                    chess_array[i][j++].setBackgroundColor(getResources().getColor(R.color.white));
                    chess_array[i][j++].setBackgroundColor(getResources().getColor(R.color.black));
                }
            }
        }
    }

    protected void resetPutTroops() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                putTroops[i][j] = 0;
            }
        }
    }

    protected void setTempPutTroops() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (putTroops[i][j] == 1) {
                    tempPutTroops[i][j] = 1;
                }
            }
        }
        resetPutTroops();
    }

    protected void optimizeTempPutTroops(final int x, final int y) {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                if (tempPutTroops[i][j] == 1) {
                    int temp = troops[i][j];
                    chess_array[i][j].setImageResource(troops[x][y]);
                    troops[i][j] = troops[x][y];
                    troops[x][y] = 0;
                    chess_array[x][y].setImageResource(0);
                    if (setKingCheck(!WHICH_TROOPS_TURNS)) {
                        tempPutTroops[i][j] = 0;
                        chess_array[x][y].setImageResource(troops[i][j]);
                        troops[x][y] = troops[i][j];
                        troops[i][j] = temp;
                        chess_array[i][j].setImageResource(troops[i][j]);
                    } else {
                        chess_array[x][y].setImageResource(troops[i][j]);
                        troops[x][y] = troops[i][j];
                        troops[i][j] = temp;
                        chess_array[i][j].setImageResource(troops[i][j]);
                    }
                }
            }
        }
    }

    protected void resetTempPutTroops() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                tempPutTroops[i][j] = 0;
            }
        }
    }

    protected void whitePawnSelected(final int i, final int j) {
        if (i == 1) {
            if (chess_array[i + 1][j].getDrawable() == null) {
                putTroops[i + 1][j] = 1;
                if (chess_array[i + 2][j].getDrawable() == null) {
                    putTroops[i + 2][j] = 1;
                }
            }
        } else {
            if (chess_array[i + 1][j].getDrawable() == null) {
                putTroops[i + 1][j] = 1;
            }
        }
        if (i + 1 < 8 && j + 1 < 8) {
            if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i + 1, j + 1)) {
                    putTroops[i + 1][j + 1] = 1;
                }
            } else {
                if (isBlackTroops(i + 1, j + 1)) {
                    putTroops[i + 1][j + 1] = 1;
                }
            }
        }
        if (i + 1 < 8 && j - 1 >= 0) {
            if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i + 1, j - 1)) {
                    putTroops[i + 1][j - 1] = 1;
                }
            } else {
                if (isBlackTroops(i + 1, j - 1)) {
                    putTroops[i + 1][j - 1] = 1;
                }
            }
        }
    }

    protected void blackPawnSelected(final int i, final int j) {
        if (i == 6) {
            if (chess_array[i - 1][j].getDrawable() == null) {
                putTroops[i - 1][j] = 1;

                if (chess_array[i - 2][j].getDrawable() == null) {
                    putTroops[i - 2][j] = 1;
                }
            }
        } else {
            if (chess_array[i - 1][j].getDrawable() == null) {
                putTroops[i - 1][j] = 1;
            }
        }
        if (i - 1 >= 0 && j - 1 >= 0) {
            if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i - 1, j - 1) && chess_array[i - 1][j - 1].getDrawable() != null) {
                    putTroops[i - 1][j - 1] = 1;
                }
            } else {
                if (isBlackTroops(i - 1, j - 1)) {
                    putTroops[i - 1][j - 1] = 1;
                }
            }
        }
        if (i - 1 >= 0 && j + 1 < 8) {
            if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i - 1, j + 1) && chess_array[i - 1][j + 1].getDrawable() != null) {
                    putTroops[i - 1][j + 1] = 1;
                }
            } else {
                if (isBlackTroops(i - 1, j + 1)) {
                    putTroops[i - 1][j + 1] = 1;
                }
            }
        }
    }

    protected void knightSelected(final int i, final int j) {
        if ((i + 2 < 8)) {
            if (j + 1 < 8) {
                if (chess_array[i + 2][j + 1].getDrawable() == null) {
                    putTroops[i + 2][j + 1] = 1;
                } else if (isBlackTroops(i, j)) {
                    if (!isBlackTroops(i + 2, j + 1)) {
                        putTroops[i + 2][j + 1] = 1;
                    }
                } else {
                    if (isBlackTroops(i + 2, j + 1)) {
                        putTroops[i + 2][j + 1] = 1;
                    }
                }
            }
            if (j - 1 >= 0) {
                if (chess_array[i + 2][j - 1].getDrawable() == null) {
                    putTroops[i + 2][j - 1] = 1;
                } else if (isBlackTroops(i, j)) {
                    if (!isBlackTroops(i + 2, j - 1)) {
                        putTroops[i + 2][j - 1] = 1;
                    }
                } else {
                    if (isBlackTroops(i + 2, j - 1)) {
                        putTroops[i + 2][j - 1] = 1;
                    }
                }
            }

        }
        if ((i - 2 >= 0)) {
            if (j + 1 < 8) {
                if (chess_array[i - 2][j + 1].getDrawable() == null) {
                    putTroops[i - 2][j + 1] = 1;
                } else if (isBlackTroops(i, j)) {
                    if (!isBlackTroops(i - 2, j + 1)) {
                        putTroops[i - 2][j + 1] = 1;
                    }
                } else {
                    if (isBlackTroops(i - 2, j + 1)) {
                        putTroops[i - 2][j + 1] = 1;
                    }
                }
            }
            if (j - 1 >= 0) {
                if (chess_array[i - 2][j - 1].getDrawable() == null) {
                    putTroops[i - 2][j - 1] = 1;
                } else if (isBlackTroops(i, j)) {
                    if (!isBlackTroops(i - 2, j - 1)) {
                        putTroops[i - 2][j - 1] = 1;
                    }
                } else {
                    if (isBlackTroops(i - 2, j - 1)) {
                        putTroops[i - 2][j - 1] = 1;
                    }
                }
            }

        }
        if ((j + 2 < 8)) {
            if (i + 1 < 8) {
                if (chess_array[i + 1][j + 2].getDrawable() == null) {
                    putTroops[i + 1][j + 2] = 1;
                } else if (isBlackTroops(i, j)) {
                    if (!isBlackTroops(i + 1, j + 2)) {
                        putTroops[i + 1][j + 2] = 1;
                    }
                } else {
                    if (isBlackTroops(i + 1, j + 2)) {
                        putTroops[i + 1][j + 2] = 1;
                    }
                }
            }
            if (i - 1 >= 0) {
                if (chess_array[i - 1][j + 2].getDrawable() == null) {
                    putTroops[i - 1][j + 2] = 1;
                } else if (isBlackTroops(i, j)) {
                    if (!isBlackTroops(i - 1, j + 2)) {
                        putTroops[i - 1][j + 2] = 1;
                    }
                } else {
                    if (isBlackTroops(i - 1, j + 2)) {
                        putTroops[i - 1][j + 2] = 1;
                    }
                }
            }
        }
        if ((j - 2 >= 0)) {
            if (i + 1 < 8) {
                if (chess_array[i + 1][j - 2].getDrawable() == null) {
                    putTroops[i + 1][j - 2] = 1;
                } else if (isBlackTroops(i, j)) {
                    if (!isBlackTroops(i + 1, j - 2)) {
                        putTroops[i + 1][j - 2] = 1;
                    }
                } else {
                    if (isBlackTroops(i + 1, j - 2)) {
                        putTroops[i + 1][j - 2] = 1;
                    }
                }
            }
            if (i - 1 >= 0) {
                if (chess_array[i - 1][j - 2].getDrawable() == null) {
                    putTroops[i - 1][j - 2] = 1;
                } else if (isBlackTroops(i, j)) {
                    if (!isBlackTroops(i - 1, j - 2)) {
                        putTroops[i - 1][j - 2] = 1;
                    }
                } else {
                    if (isBlackTroops(i - 1, j - 2)) {
                        putTroops[i - 1][j - 2] = 1;
                    }
                }
            }
        }


    }

    protected void rookSelected(final int i, final int j) {
        for (int k = i + 1; k < 8; ++k) {
            if (chess_array[k][j].getDrawable() == null) {
                putTroops[k][j] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(k, j)) {
                    putTroops[k][j] = 1;
                }
                break;
            } else if (!isBlackTroops(i, j)) {
                if (isBlackTroops(k, j)) {
                    putTroops[k][j] = 1;
                }
                break;
            } else {
                break;
            }
        }
        for (int k = i - 1; k >= 0; --k) {
            if (chess_array[k][j].getDrawable() == null) {
                putTroops[k][j] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(k, j)) {
                    putTroops[k][j] = 1;
                }
                break;
            } else if (!isBlackTroops(i, j)) {
                if (isBlackTroops(k, j)) {
                    putTroops[k][j] = 1;
                }
                break;
            } else {
                break;
            }
        }
        for (int k = j + 1; k < 8; ++k) {
            if (chess_array[i][k].getDrawable() == null) {
                putTroops[i][k] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i, k)) {
                    putTroops[i][k] = 1;
                }
                break;
            } else if (!isBlackTroops(i, j)) {
                if (isBlackTroops(i, k)) {
                    putTroops[i][k] = 1;
                }
                break;
            } else {
                break;
            }
        }
        for (int k = j - 1; k >= 0; --k) {
            if (chess_array[i][k].getDrawable() == null) {
                putTroops[i][k] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i, k)) {
                    putTroops[i][k] = 1;
                }
                break;
            } else if (!isBlackTroops(i, j)) {
                if (isBlackTroops(i, k)) {
                    putTroops[i][k] = 1;
                }
                break;
            } else {
                break;
            }
        }
    }

    protected void bishopSelected(final int i, final int j) {
        for (int x = i + 1, y = j + 1; x < 8 && y < 8; ++x, ++y) {
            if (chess_array[x][y].getDrawable() == null) {
                putTroops[x][y] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(x, y)) {
                    putTroops[x][y] = 1;
                }
                break;
            } else if (!isBlackTroops(i, j)) {
                if (isBlackTroops(x, y)) {
                    putTroops[x][y] = 1;
                }
                break;
            } else {
                break;
            }
        }
        for (int x = i - 1, y = j - 1; x >= 0 && y >= 0; --x, --y) {
            if (chess_array[x][y].getDrawable() == null) {
                putTroops[x][y] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(x, y)) {
                    putTroops[x][y] = 1;
                }
                break;
            } else if (!isBlackTroops(i, j)) {
                if (isBlackTroops(x, y)) {
                    putTroops[x][y] = 1;
                }
                break;
            } else {
                break;
            }
        }
        for (int x = i + 1, y = j - 1; x < 8 && y >= 0; ++x, --y) {
            if (chess_array[x][y].getDrawable() == null) {
                putTroops[x][y] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(x, y)) {
                    putTroops[x][y] = 1;
                }
                break;
            } else if (!isBlackTroops(i, j)) {
                if (isBlackTroops(x, y)) {
                    putTroops[x][y] = 1;
                }
                break;
            } else {
                break;
            }
        }
        for (int x = i - 1, y = j + 1; x >= 0 && y < 8; --x, ++y) {
            if (chess_array[x][y].getDrawable() == null) {
                putTroops[x][y] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(x, y)) {
                    putTroops[x][y] = 1;
                }
                break;
            } else if (!isBlackTroops(i, j)) {
                if (isBlackTroops(x, y)) {
                    putTroops[x][y] = 1;
                }
                break;
            } else {
                break;
            }
        }
    }

    protected void queenSelected(final int i, final int j) {
        bishopSelected(i, j);
        rookSelected(i, j);
    }

    protected void kingSelected(final int i, final int j) {
        if (i + 1 < 8) {
            if (chess_array[i + 1][j].getDrawable() == null) {
                putTroops[i + 1][j] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i + 1, j)) {
                    putTroops[i + 1][j] = 1;
                }
            } else {
                if (isBlackTroops(i + 1, j)) {
                    putTroops[i + 1][j] = 1;
                }
            }
        }
        if (i - 1 >= 0) {
            if (chess_array[i - 1][j].getDrawable() == null) {
                putTroops[i - 1][j] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i - 1, j)) {
                    putTroops[i - 1][j] = 1;
                }
            } else {
                if (isBlackTroops(i - 1, j)) {
                    putTroops[i - 1][j] = 1;
                }
            }
        }
        if (j + 1 < 8) {
            if (chess_array[i][j + 1].getDrawable() == null) {
                putTroops[i][j + 1] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i, j + 1)) {
                    putTroops[i][j + 1] = 1;
                }
            } else {
                if (isBlackTroops(i, j + 1)) {
                    putTroops[i][j + 1] = 1;
                }
            }
        }
        if (j - 1 >= 0) {
            if (chess_array[i][j - 1].getDrawable() == null) {
                putTroops[i][j - 1] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i, j - 1)) {
                    putTroops[i][j - 1] = 1;
                }
            } else {
                if (isBlackTroops(i, j - 1)) {
                    putTroops[i][j - 1] = 1;
                }
            }
        }
        if (i + 1 < 8 && j + 1 < 8) {
            if (chess_array[i + 1][j + 1].getDrawable() == null) {
                putTroops[i + 1][j + 1] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i + 1, j + 1)) {
                    putTroops[i + 1][j + 1] = 1;
                }
            } else {
                if (isBlackTroops(i + 1, j + 1)) {
                    putTroops[i + 1][j + 1] = 1;
                }
            }
        }
        if (i - 1 >= 0 && j - 1 >= 0) {
            if (chess_array[i - 1][j - 1].getDrawable() == null) {
                putTroops[i - 1][j - 1] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i - 1, j - 1)) {
                    putTroops[i - 1][j - 1] = 1;
                }
            } else {
                if (isBlackTroops(i - 1, j - 1)) {
                    putTroops[i - 1][j - 1] = 1;
                }
            }
        }
        if (i + 1 < 8 && j - 1 >= 0) {
            if (chess_array[i + 1][j - 1].getDrawable() == null) {
                putTroops[i + 1][j - 1] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i + 1, j - 1)) {
                    putTroops[i + 1][j - 1] = 1;
                }
            } else {
                if (isBlackTroops(i + 1, j - 1)) {
                    putTroops[i + 1][j - 1] = 1;
                }
            }
        }
        if (i - 1 >= 0 && j + 1 < 8) {
            if (chess_array[i - 1][j + 1].getDrawable() == null) {
                putTroops[i - 1][j + 1] = 1;
            } else if (isBlackTroops(i, j)) {
                if (!isBlackTroops(i - 1, j + 1)) {
                    putTroops[i - 1][j + 1] = 1;
                }
            } else {
                if (isBlackTroops(i - 1, j + 1)) {
                    putTroops[i - 1][j + 1] = 1;
                }
            }
        }
    }

    protected boolean whichTroopsTurns() {
        return WHICH_TROOPS_TURNS;
    }

    protected boolean setKingCheck(boolean temp) {
        if (!temp) {
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    resetPutTroops();
                    if (!isBlackTroops(i, j) && chess_array[i][j].getDrawable() != null) {
                        if (troops[i][j] == WHITE_PAWN) {
                            whitePawnSelected(i, j);
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; ++y) {
                                    if (putTroops[x][y] == 1 && troops[x][y] == BLACK_KING) {
                                        resetPutTroops();
                                        return true;
                                    }
                                }
                            }
                        } else if (troops[i][j] == WHITE_ROOK) {
                            rookSelected(i, j);
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; ++y) {
                                    if (putTroops[x][y] == 1 && troops[x][y] == BLACK_KING) {
                                        resetPutTroops();
                                        return true;
                                    }
                                }
                            }
                        } else if (troops[i][j] == WHITE_BISHOP) {
                            bishopSelected(i, j);
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; ++y) {
                                    if (putTroops[x][y] == 1 && troops[x][y] == BLACK_KING) {
                                        resetPutTroops();
                                        return true;
                                    }
                                }
                            }
                        } else if (troops[i][j] == WHITE_KNIGHT) {
                            knightSelected(i, j);
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; ++y) {
                                    if (putTroops[x][y] == 1 && troops[x][y] == BLACK_KING) {
                                        resetPutTroops();
                                        return true;
                                    }
                                }
                            }
                        } else {
                            if (troops[i][j] == WHITE_QUEEN) {
                                queenSelected(i, j);
                                for (int x = 0; x < 8; x++) {
                                    for (int y = 0; y < 8; ++y) {
                                        if (putTroops[x][y] == 1 && troops[x][y] == BLACK_KING) {
                                            resetPutTroops();
                                            return true;
                                        }
                                    }
                                }
                            } else {
                                if (troops[i][j] == WHITE_KING) {
                                    kingSelected(i, j);
                                    for (int x = 0; x < 8; x++) {
                                        for (int y = 0; y < 8; ++y) {
                                            if (putTroops[x][y] == 1 && troops[x][y] == BLACK_KING) {
                                                resetPutTroops();
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    if (isBlackTroops(i, j)) {
                        if (troops[i][j] == BLACK_PAWN) {
                            blackPawnSelected(i, j);
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; ++y) {
                                    if (putTroops[x][y] == 1 && troops[x][y] == WHITE_KING) {
                                        resetPutTroops();
                                        return true;
                                    }
                                }
                            }
                        } else if (troops[i][j] == BLACK_ROOK) {
                            rookSelected(i, j);
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; ++y) {
                                    if (putTroops[x][y] == 1 && troops[x][y] == WHITE_KING) {
                                        resetPutTroops();
                                        return true;
                                    }
                                }
                            }
                        } else if (troops[i][j] == BLACK_BISHOP) {
                            bishopSelected(i, j);
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; ++y) {
                                    if (putTroops[x][y] == 1 && troops[x][y] == WHITE_KING) {
                                        resetPutTroops();
                                        return true;
                                    }
                                }
                            }
                        } else if (troops[i][j] == BLACK_KNIGHT) {
                            knightSelected(i, j);
                            for (int x = 0; x < 8; x++) {
                                for (int y = 0; y < 8; ++y) {
                                    if (putTroops[x][y] == 1 && troops[x][y] == WHITE_KING) {
                                        resetPutTroops();
                                        return true;
                                    }
                                }
                            }
                        } else {
                            if (troops[i][j] == BLACK_QUEEN) {
                                queenSelected(i, j);
                                for (int x = 0; x < 8; x++) {
                                    for (int y = 0; y < 8; ++y) {
                                        if (putTroops[x][y] == 1 && troops[x][y] == WHITE_KING) {
                                            resetPutTroops();
                                            return true;
                                        }
                                    }
                                }
                            } else {
                                if (troops[i][j] == BLACK_KING) {
                                    kingSelected(i, j);
                                    for (int x = 0; x < 8; x++) {
                                        for (int y = 0; y < 8; ++y) {
                                            if (putTroops[x][y] == 1 && troops[x][y] == WHITE_KING) {
                                                resetPutTroops();
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        resetPutTroops();
        return false;
    }

    protected boolean isCheckMate(boolean IS_WHITE) {
        if (IS_WHITE) {
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    if (!isBlackTroops(i, j) && chess_array[i][j].getDrawable() != null) {
                        if (troops[i][j] == WHITE_PAWN) {
                            whitePawnSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                        } else if (troops[i][j] == WHITE_ROOK) {
                            rookSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                        } else if (troops[i][j] == WHITE_KNIGHT) {
                            knightSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                        } else if (troops[i][j] == WHITE_BISHOP) {
                            bishopSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                        } else if (troops[i][j] == WHITE_QUEEN) {
                            queenSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                        } else if (troops[i][j] == WHITE_KING) {
                            kingSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                        }
                    }
                }
            }
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    if (tempPutTroops[i][j] == 1) {
                        resetTempPutTroops();
                        return false;
                    }
                }
            }
        } else {
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    if (isBlackTroops(i, j)) {
                        if (troops[i][j] == BLACK_PAWN) {
                            blackPawnSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                            tempPutTroops[i][j] = 0;
                        } else if (troops[i][j] == BLACK_ROOK) {
                            rookSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                            tempPutTroops[i][j] = 0;
                        } else if (troops[i][j] == BLACK_KNIGHT) {
                            knightSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                            tempPutTroops[i][j] = 0;
                        } else if (troops[i][j] == BLACK_BISHOP) {
                            bishopSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                            tempPutTroops[i][j] = 0;
                        } else if (troops[i][j] == BLACK_QUEEN) {
                            queenSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                            tempPutTroops[i][j] = 0;
                        } else if (troops[i][j] == BLACK_KING) {
                            kingSelected(i, j);
                            setTempPutTroops();
                            optimizeTempPutTroops(i, j);
                            tempPutTroops[i][j] = 0;
                        }
                    }
                }
            }
            for (int i = 0; i < 8; ++i) {
                for (int j = 0; j < 8; ++j) {
                    if (tempPutTroops[i][j] == 1) {
                        resetTempPutTroops();
                        return false;
                    }
                }
            }
        }
        resetPutTroops();
        return true;
    }


    protected boolean isStalemate(boolean WHITE) {
        return isCheckMate(WHITE);
    }

    protected void resetTroops() {
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                troops[i][j] = 0;
            }
        }
    }

    protected void resetChessGame() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                chess_array[i][j].setImageResource(0);
            }
        }
        IS_TROOPS_SELECTED = false;
        WHICH_TROOPS_TURNS = false;
        KING_CHECK = false;
        CHECK_MATE = false;
        check_event.setImageResource(0);
        resetColor();
        resetTempPutTroops();
        resetPutTroops();
        resetTroops();
        setInitialTroops();
    }
}


