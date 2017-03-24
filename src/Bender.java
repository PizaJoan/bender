import java.util.Arrays;

/**
 * Created by pizajoan on 15/3/17.
 */
public class Bender {
    private char[][] mapa;
    private int[] positionX;
    private int[] position$;
    private boolean iteratorPicked = false;

    // Constructor: ens passen el mapa en forma d'String
    public Bender(String mapa) {
        String[] correctMap = mapa.split("\n");
        this.mapa = new char[correctMap.length][correctMap[0].length()];
        int cont = 0;
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[0].length; j++) {
                this.mapa[i][j] = correctMap[i].charAt(cont++);
                if (cont == this.mapa[0].length) cont = 0;
            }
        }
        this.positionX = getChar('X');
        this.position$ = getChar('$');
    }

    // Navegar fins a l'objectiu («$»).
    // El valor retornat pel mètode consisteix en una cadena de
    // caràcters on cada lletra pot tenir els valors «S», «N», «W» o «E»,
    // segons la posició del robot a cada moment.
    public String run() {
        StringBuilder moved = new StringBuilder();
        char actualLetter;
        if (Arrays.deepToString(this.mapa).contains("T") || Arrays.deepToString(this.mapa).contains("I")) {
            int[] positionT = getChar('T');
            int[] positionI = getChar('I');
            if (positionI[0] > 0) {
                while (positionI[0] != positionX[0] || positionI[1] != positionX[1]) {
                    actualLetter = whereGo();
                    moved.append(move(positionI, actualLetter));
                }
                this.iteratorPicked = true;
            }
            if (positionT[0] > 0) {
                if (!iteratorPicked) {
                    while (positionT[0] != positionX[0] || positionT[1] != positionX[1]) {
                        actualLetter = whereGo();
                        moved.append(move(positionT, actualLetter));
                        if (this.positionX[0] == this.position$[0] && this.positionX[1] == this.position$[1]) break;
                    }
                } else {
                    while (positionT[2] != positionX[0] || positionT[3] != positionX[1]) {
                        actualLetter = whereGo();
                        moved.append(move(positionT, actualLetter));
                        if (this.positionX[0] == this.position$[0] && this.positionX[1] == this.position$[1]) break;
                    }
                }
            }
        }
        while (this.positionX[0] != this.position$[0] || this.positionX[1] != this.position$[1]) {
            actualLetter = whereGo();
            moved.append(move(position$, actualLetter));
        }
        return moved.toString();
    }

    private int[] getChar(char lletra) {
        int[] charPos = new int[2];
        if (lletra == 'T') charPos = new int[4];
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[0].length; j++) {
                if (this.mapa[i][j] == lletra && this.mapa[charPos[0]][charPos[1]] != 'T') {
                    charPos[0] = i;
                    charPos[1] = j;
                    if (lletra == 'T') break;
                    return charPos;
                } else if (this.mapa[i][j] == lletra) {
                    charPos[2] = i;
                    charPos[3] = j;
                    return charPos;
                }
            }
        }
        return charPos;
    }

    private StringBuilder move(int[] lookingFor, char actualLetter) {
        StringBuilder moving = new StringBuilder();
        while (touchWall(actualLetter)) {
            if (this.positionX[0] == this.position$[0] && this.positionX[1] == this.position$[1]) return moving;
            moving.append(actualLetter);
            calculation(this.positionX, actualLetter);
            if (lookingFor.length > 2 && lookingFor[2] == this.positionX[0] && this.positionX[1] == lookingFor[3] && !this.iteratorPicked) {
                this.positionX[0] = lookingFor[0];
                this.positionX[1] = lookingFor[1];
            } else if (lookingFor.length > 2 && this.positionX[0] == lookingFor[0] && this.positionX[1] == lookingFor[1] && this.iteratorPicked) {
                this.positionX[0] = lookingFor[2];
                this.positionX[1] = lookingFor[3];
            }
        }
        return moving;
    }

    private boolean touchWall(char c) {
        if (c == 'S') return this.mapa[this.positionX[0] + 1][this.positionX[1]] != '#';
        if (c == 'E') return this.mapa[this.positionX[0]][this.positionX[1] + 1] != '#';
        if (c == 'N') return this.mapa[this.positionX[0] - 1][this.positionX[1]] != '#';
        return this.mapa[this.positionX[0]][this.positionX[1] - 1] != '#';
    }

    private int[] calculation(int[] posicio, char lletra) {
        if (lletra == 'S') {
            posicio[0]++;
            return posicio;
        }
        if (lletra == 'E') {
            posicio[1]++;
            return posicio;
        }
        if (lletra == 'N') {
            posicio[0]--;
            return posicio;
        }
        posicio[1]--;
        return posicio;
    }

    private char whereGo() {
        if (!this.iteratorPicked) {
            if (this.mapa[this.positionX[0] + 1][this.positionX[1]] != '#') return 'S';
            if (this.mapa[this.positionX[0]][this.positionX[1] + 1] != '#') return 'E';
            if (this.mapa[this.positionX[0] - 1][this.positionX[1]] != '#') return 'N';
            return 'W';
        }
        if (this.mapa[this.positionX[0] - 1][this.positionX[1]] != '#') return 'N';
        if (this.mapa[this.positionX[0]][this.positionX[1] - 1] != '#') return 'W';
        if (this.mapa[this.positionX[0] + 1][this.positionX[1]] != '#') return 'S';
        return 'E';
    }

    public int bestRun() {
        int[][] numericMap = new int[this.mapa.length][this.mapa[0].length];
        if (this.positionX[0] + 1 < this.mapa.length && this.mapa[this.positionX[0] + 1][this.positionX[1]] != '#') numericMap[this.positionX[0] + 1][this.positionX[1]] = 1;
        if (this.positionX[1] + 1 < this.mapa[0].length && this.mapa[this.positionX[0]][this.positionX[1] + 1] != '#') numericMap[this.positionX[0]][this.positionX[1] + 1] = 1;
        if (this.positionX[0] - 1 > 0 && this.mapa[this.positionX[0] - 1][this.positionX[1]] != '#') numericMap[this.positionX[0] - 1][this.positionX[1]] = 1;
        if (this.positionX[1] - 1 > 0 && this.mapa[this.positionX[0]][this.positionX[1] - 1] != '#') numericMap[this.positionX[0]][this.positionX[1] - 1] = 1;
        int cont = 1;
        while (numericMap[position$[0]][position$[1]] == 0) {
            editMap(numericMap, cont);
            cont++;
        }
        return numericMap[position$[0]][position$[1]];
    }

    private void editMap(int[][] numericMap, int contador) {
        int[] tes = getChar('T');
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[0].length; j++) {
                if (i == this.positionX[0] && j == this.positionX[1] || this.mapa[i][j] == '#') continue;
                if (tes[0] > 0 && numericMap[tes[0]][tes[1]] > 0) {
                    numericMap[tes[2]][tes[3]] = numericMap[i][j];
                    tes[0] = 0;
                } else {
                    if (i + 1 < this.mapa.length && this.mapa[i][j] != '#' && this.mapa[i + 1][j] != '#' && numericMap[i][j] == contador) numericMap[i + 1][j] = numericMap[i][j] + 1;
                    if (j + 1 < this.mapa[0].length && this.mapa[i][j] != '#' && this.mapa[i][j + 1] != '#' && numericMap[i][j] == contador) numericMap[i][j + 1] = numericMap[i][j] + 1;
                    if (i - 1 > 0 && this.mapa[i][j] != '#' && this.mapa[i - 1][j] != '#' && numericMap[i][j] == contador) numericMap[i - 1][j] = numericMap[i][j] + 1;
                    if (j - 1 > 0 && this.mapa[i][j] != '#' && this.mapa[i][j - 1] != '#' && numericMap[i][j] == contador) numericMap[i][j - 1] = numericMap[i][j] + 1;
                }
            }

        }
    }
}
