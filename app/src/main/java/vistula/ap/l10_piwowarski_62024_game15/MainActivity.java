package vistula.ap.l10_piwowarski_62024_game15;

import android.graphics.Point;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    int[] numbersap;
    int[] numberAfterPermutationap;
    int ColumnsButtonssap = 4;
    int RowsButtonsap = 4;
    int totalButtonsap = ColumnsButtonssap * RowsButtonsap;
    int screenWidthap;
    int screenHeightap;
    Button[][] buttonsap;
    int buttonsIDap[][];
    int leftap = 160;
    int topap = 220;
    int[] playerNumberap;
    int resultap;
    int moveap = 0, scoreap = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        Spinner spinnerColumnsap = (Spinner)findViewById(R.id.SpinnerNumberColumnssap);
        Spinner spinnerRowsap = (Spinner)findViewById(R.id.SpinnerNumberRowsap);
        spinnerColumnsap.setSelection(3);
        spinnerRowsap.setSelection(3);
        Ready();
    }
    private void makeTotalButtons(){
        Spinner spinnerColumnsap = (Spinner)findViewById(R.id.SpinnerNumberColumnssap);
        Spinner spinnerRowsap = (Spinner)findViewById(R.id.SpinnerNumberRowsap);
        ColumnsButtonssap = Integer.parseInt(spinnerColumnsap.getSelectedItem().toString());
        RowsButtonsap = Integer.parseInt(spinnerRowsap.getSelectedItem().toString());
        totalButtonsap = ColumnsButtonssap * RowsButtonsap;
    }
    private void Ready(){
        WindowManager windowManagerap = getWindowManager();
        Point sizeap = new Point();
        windowManagerap.getDefaultDisplay().getSize(sizeap);
        screenWidthap = sizeap.x;
        screenHeightap = sizeap.y;
        makeTotalButtons();
        numbersap = new int[totalButtonsap];
        for (int i = 0; i< totalButtonsap; i++){
            numbersap[i]=i;
        }
        numberAfterPermutationap = numbersap.clone();
        playerNumberap = new int[totalButtonsap];
        buttonsap = new Button[RowsButtonsap][ColumnsButtonssap];
        buttonsIDap = new int[RowsButtonsap][ColumnsButtonssap];
        startButtonsap();
        resultap = 0;
        moveap = 0;
        TextView numMoveap = (TextView) findViewById(R.id.numMoveap);
        numMoveap.setText(Integer.toString(moveap));
    }
    private void startButtonsap(){
        makeTotalButtons();
        ConstraintLayout apconstraintLayout = (ConstraintLayout) findViewById(R.id.apconstraintLayout);
        apconstraintLayout.removeAllViews();
        ConstraintLayout.LayoutParams apparams;
        int buttonWidth = 150, buttonHeight = 100;

        WindowManager windowManager = getWindowManager();
        Point sizeap = new Point();
        windowManager.getDefaultDisplay().getSize(sizeap);
        screenWidthap = sizeap.x;
        screenHeightap = sizeap.y;
        leftap = Math.round((screenWidthap - buttonWidth * ColumnsButtonssap) / 2);

        for (int i = 0; i < RowsButtonsap; i++) {
            for (int j = 0; j < ColumnsButtonssap; j++) {
                buttonsap[i][j] = new Button(this);
                buttonsap[i][j].setId(View.generateViewId());
                buttonsIDap[i][j] = buttonsap[i][j].getId();
                apparams = new ConstraintLayout.LayoutParams(buttonWidth, buttonHeight);
                buttonsap[i][j].setLayoutParams(apparams);
                apconstraintLayout.addView(buttonsap[i][j]);
                ConstraintSet apset = new ConstraintSet();
                apset.clone(apconstraintLayout);
                if (j == 0) {
                    apset.connect(buttonsap[i][j].getId(), ConstraintSet.LEFT, apconstraintLayout.getId(), ConstraintSet.LEFT, leftap);
                } else {
                    apset.connect(buttonsap[i][j].getId(), ConstraintSet.LEFT, buttonsap[i][j - 1].getId(), ConstraintSet.RIGHT, 0);
                }
                if (i == 0) {
                    apset.connect(buttonsap[i][j].getId(), ConstraintSet.TOP, apconstraintLayout.getId(), ConstraintSet.TOP, topap);
                } else {
                    apset.connect(buttonsap[i][j].getId(), ConstraintSet.TOP, buttonsap[i - 1][j].getId(), ConstraintSet.BOTTOM, 0);
                }
                apset.applyTo(apconstraintLayout);
                buttonsap[i][j].setOnClickListener(apbuttonClickListener);
            }
        }
        numbersButtonsap();
        calculateInitialScore();
    }

    final View.OnClickListener apbuttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button button = (Button) view;
            int buttonRow = -1, buttonCol = -1;

            for (int i = 0; i < RowsButtonsap; i++) {
                for (int j = 0; j < ColumnsButtonssap; j++) {
                    if (buttonsIDap[i][j] == button.getId()) {
                        buttonRow = i;
                        buttonCol = j;
                    }
                }
            }

            trySwap(buttonRow, buttonCol, buttonRow, buttonCol - 1);
            trySwap(buttonRow, buttonCol, buttonRow, buttonCol + 1);
            trySwap(buttonRow, buttonCol, buttonRow - 1, buttonCol);
            trySwap(buttonRow, buttonCol, buttonRow + 1, buttonCol);

            checkGameCompletion();
        }

        private void trySwap(int fromRow, int fromCol, int toRow, int toCol) {
            makeTotalButtons();
            if (toRow >= 0 && toRow < RowsButtonsap && toCol >= 0 && toCol < ColumnsButtonssap) {
                Button fromButton = buttonsap[fromRow][fromCol];
                Button toButton = buttonsap[toRow][toCol];
                if (toButton.getText().toString().equals("0")) {
                    int fromCorrectNumber = fromRow * ColumnsButtonssap + fromCol + 1;
                    if (fromCorrectNumber == totalButtonsap) fromCorrectNumber = 0;
                    boolean fromWasCorrect = fromButton.getText().toString().equals(String.valueOf(fromCorrectNumber));

                    int toCorrectNumber = toRow * ColumnsButtonssap + toCol + 1;
                    if (toCorrectNumber == totalButtonsap) toCorrectNumber = 0;
                    boolean toWasCorrect = toButton.getText().toString().equals(String.valueOf(toCorrectNumber));

                    CharSequence fromText = fromButton.getText();
                    fromButton.setText("0");
                    toButton.setText(fromText);

                    boolean fromIsNowCorrect = fromButton.getText().toString().equals(String.valueOf(fromCorrectNumber));
                    boolean toIsNowCorrect = toButton.getText().toString().equals(String.valueOf(toCorrectNumber));

                    if (fromWasCorrect && !fromIsNowCorrect) {
                        scoreap--;
                    }
                    if (!toWasCorrect && toIsNowCorrect) {
                        scoreap++;
                    }
                    TextView numScoreap = (TextView) findViewById(R.id.numScoreap);
                    numScoreap.setText(Integer.toString(scoreap));
                    TextView numMoveap = (TextView) findViewById(R.id.numMoveap);
                    moveap++;
                    numMoveap.setText(Integer.toString(moveap));
                }
            }
        }
    };

    private void checkGameCompletion() {
        makeTotalButtons();
        boolean isSuccess = true;

        for (int i = 0; i < totalButtonsap - 1; i++) {
            int row = i / ColumnsButtonssap;
            int col = i % ColumnsButtonssap;
            String buttonText = buttonsap[row][col].getText().toString();

            if (i == totalButtonsap - 3) {
                if (!buttonText.equals(String.valueOf(totalButtonsap - 1))) {
                    isSuccess = false;
                    break;
                }
            } else if (i == totalButtonsap - 2) {
                if (!buttonText.equals(String.valueOf(totalButtonsap - 2))) {
                    isSuccess = false;
                    break;
                }
            } else {
                if (!buttonText.equals(String.valueOf(i + 1))) {
                    isSuccess = false;
                    break;
                }
            }
        }
        if (isSuccess) {
            int lastRow = (totalButtonsap - 1) / ColumnsButtonssap;
            int lastCol = (totalButtonsap - 1) % ColumnsButtonssap;
            String lastButtonText = buttonsap[lastRow][lastCol].getText().toString();

            if (!lastButtonText.equals("0")) {
                isSuccess = false;
            }
        }

        if (isSuccess) {
            Toast.makeText(this, "Congratulations! You've completed the game.", Toast.LENGTH_LONG).show();
        }
    }

    private void numbersButtonsap() {
        String strap;
        makeTotalButtons();
        for (int i = 0; i < RowsButtonsap; i++) {
            for (int j = 0; j < ColumnsButtonssap; j++) {
                strap = Integer.toString(numberAfterPermutationap[i * ColumnsButtonssap + j]);
                buttonsap[i][j].setText(strap);
            }
        }
    }

    public void makePermutationap(){
        Ready();
        numberAfterPermutationap = ArrayPermuatation.shuffleFisherYeats(numbersap);
        numbersButtonsap();
        calculateInitialScore();
    }

    private void calculateInitialScore() {
        int initialScore = 0;
        makeTotalButtons();

        for (int i = 0; i < totalButtonsap; i++) {
            int row = i / ColumnsButtonssap;
            int col = i % ColumnsButtonssap;
            Button currentButton = buttonsap[row][col];
            int expectedNumber = i + 1;
            if (expectedNumber == totalButtonsap) {
                expectedNumber = 0;
            }

            if (currentButton.getText().toString().equals(String.valueOf(expectedNumber))) {
                initialScore++;
            }
        }
        scoreap = initialScore;
        TextView numScoreap = (TextView) findViewById(R.id.numScoreap);
        numScoreap.setText(Integer.toString(scoreap));
    }

    public void NewGameap(View view){
        makePermutationap();
    }
}
