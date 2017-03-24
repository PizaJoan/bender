import java.util.Arrays;

/**
 * Created by pizajoan on 15/3/17.
 */
public class Bender {
    private char[][] mapa;
    private int[] xpos;
    private int[] dollaPos;
    private boolean itocada = false;

    // Constructor: ens passen el mapa en forma d'String
    public Bender(String mapa) {
        String[] mapabe = mapa.split("\n");
        this.mapa = new char[mapabe.length][mapabe[0].length()];
        int cont = 0;
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[0].length; j++) {
                this.mapa[i][j] = mapabe[i].charAt(cont++);
                if (cont == this.mapa[0].length) cont = 0;
            }
        }
        this.xpos = getChar('X');
        this.dollaPos = getChar('$');
    }

    // Navegar fins a l'objectiu («$»).
    // El valor retornat pel mètode consisteix en una cadena de
    // caràcters on cada lletra pot tenir els valors «S», «N», «W» o «E»,
    // segons la posició del robot a cada moment.
    public String run() {
        StringBuilder sb = new StringBuilder();
        char lletra;
        if (Arrays.deepToString(this.mapa).contains("T") || Arrays.deepToString(this.mapa).contains("I")) {
            int[] tpos = getChar('T');
            int[] ipos = getChar('I');
            if (ipos[0] > 0) {
                while (ipos[0] != xpos[0] || ipos[1] != xpos[1]) {
                    lletra = whereGo(this.xpos, this.itocada);
                    sb.append(move(ipos, lletra));
                }
                this.itocada = true;
            }
            if (tpos[0] > 0) {
                if (!itocada) {
                    while (tpos[0] != xpos[0] || tpos[1] != xpos[1]) {
                        lletra = whereGo(this.xpos, this.itocada);
                        sb.append(move(tpos, lletra));
                        if (this.xpos[0] == this.dollaPos[0] && this.xpos[1] == this.dollaPos[1]) break;
                    }
                } else {
                    while (tpos[2] != xpos[0] || tpos[3] != xpos[1]) {
                        lletra = whereGo(this.xpos, this.itocada);
                        sb.append(move(tpos, lletra));
                        if (this.xpos[0] == this.dollaPos[0] && this.xpos[1] == this.dollaPos[1]) break;
                    }
                }
            }
        }
        while (this.xpos[0] != this.dollaPos[0] || this.xpos[1] != this.dollaPos[1]) {
            lletra = whereGo(this.xpos, this.itocada);
            sb.append(move(dollaPos, lletra));
        }
        return sb.toString();
    }

    private int[] getChar(char lletra) {
        int[] position = new int[2];
        if (lletra == 'T') position = new int[4];
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[0].length; j++) {
                if (this.mapa[i][j] == lletra && this.mapa[position[0]][position[1]] != 'T') {
                    position[0] = i;
                    position[1] = j;
                    if (lletra == 'T') break;
                    return position;
                } else if (this.mapa[i][j] == lletra) {
                    position[2] = i;
                    position[3] = j;
                    return position;
                }
            }
        }
        return position;
    }

    private StringBuilder move(int[] interes, char lletra) {
        StringBuilder sb = new StringBuilder();
        while (tocamParet(this.xpos, lletra)) {
            if (this.xpos[0] == this.dollaPos[0] && this.xpos[1] == this.dollaPos[1]) return sb;
            sb.append(lletra);
            calcul(this.xpos, lletra);
            if (interes.length > 2 && interes[2] == this.xpos[0] && this.xpos[1] == interes[3] && !this.itocada) {
                this.xpos[0] = interes[0];
                this.xpos[1] = interes[1];
            } else if (interes.length > 2 && this.xpos[0] == interes[0] && this.xpos[1] == interes[1] && this.itocada) {
                this.xpos[0] = interes[2];
                this.xpos[1] = interes[3];
            }
        }
        return sb;
    }

    private boolean tocamParet(int[] ar, char c) {
        if (c == 'S') return this.mapa[ar[0] + 1][ar[1]] != '#';
        if (c == 'E') return this.mapa[ar[0]][ar[1] + 1] != '#';
        if (c == 'N') return this.mapa[ar[0] - 1][ar[1]] != '#';
        return this.mapa[ar[0]][ar[1] - 1] != '#';
    }

    private int[] calcul(int[] posicio, char lletra) {
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

    private char whereGo(int[] robot, boolean tocat) {
        if (!tocat) {
            if (this.mapa[robot[0] + 1][robot[1]] != '#') return 'S';
            if (this.mapa[robot[0]][robot[1] + 1] != '#') return 'E';
            if (this.mapa[robot[0] - 1][robot[1]] != '#') return 'N';
            return 'W';
        }
        if (this.mapa[robot[0] - 1][robot[1]] != '#') return 'N';
        if (this.mapa[robot[0]][robot[1] - 1] != '#') return 'W';
        if (this.mapa[robot[0] + 1][robot[1]] != '#') return 'S';
        return 'E';
    }

    public int bestRun() {
        int[][] ar = new int[this.mapa.length][this.mapa[0].length];
        if (xpos[0] + 1 < this.mapa.length && this.mapa[xpos[0] + 1][xpos[1]] != '#') ar[xpos[0] + 1][xpos[1]] = 1;
        if (xpos[1] + 1 < this.mapa[0].length && this.mapa[xpos[0]][xpos[1] + 1] != '#') ar[xpos[0]][xpos[1] + 1] = 1;
        if (xpos[0] - 1 > 0 && this.mapa[xpos[0] - 1][xpos[1]] != '#') ar[xpos[0] - 1][xpos[1]] = 1;
        if (xpos[1] - 1 > 0 && this.mapa[xpos[0]][xpos[1] - 1] != '#') ar[xpos[0]][xpos[1] - 1] = 1;
        int contador = 1;
        while (ar[dollaPos[0]][dollaPos[1]] == 0) {
            tocarMapa(ar, contador);
            contador++;
        }
        return ar[dollaPos[0]][dollaPos[1]];
    }

    private void tocarMapa(int[][] ar, int contador) {
        int[] tes = getChar('T');
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[0].length; j++) {
                if (i == xpos[0] && j == xpos[1] || this.mapa[i][j] == '#') continue;
                if (tes[0] > 0 && ar[tes[0]][tes[1]] > 0) {
                        ar[tes[2]][tes[3]] = ar[i][j];
                        tes[0] = 0;
                } else {
                        if (i + 1 < this.mapa.length && this.mapa[i][j] != '#' && this.mapa[i + 1][j] != '#' && ar[i][j] == contador) ar[i + 1][j] = ar[i][j] + 1;
                        if (j + 1 < this.mapa[0].length && this.mapa[i][j] != '#' && this.mapa[i][j + 1] != '#' && ar[i][j] == contador) ar[i][j + 1] = ar[i][j] + 1;
                        if (i - 1 > 0 && this.mapa[i][j] != '#' && this.mapa[i - 1][j] != '#' && ar[i][j] == contador) ar[i - 1][j] = ar[i][j] + 1;
                        if (j - 1 > 0 && this.mapa[i][j] != '#' && this.mapa[i][j - 1] != '#' && ar[i][j] == contador) ar[i][j - 1] = ar[i][j] + 1;
                    }
                }

            }
        }
    }
