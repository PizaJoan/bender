import java.util.Arrays;

/**
 * Created by pizajoan on 15/3/17.
 */
public class Bender {
    //Mapa bidimensional de chars
    private char[][] mapa;
    //Arrays de int per guardar la posició on es troba el punt de partida i el punt d'interes
    private int[] positionX;
    private int[] position$;
    //Un boolean que guardarem si hem tocat un inversor
    private boolean iteratorPicked = false;

    // Constructor: ens passen el mapa en forma d'String
    public Bender(String mapa) {
        //Ficam el mapa dins un array de Strings separat per el '\n'
        String[] correctMap = mapa.split("\n");
        //Cream el mapa de chars amb la longitud del mapa
        this.mapa = new char[correctMap.length][correctMap[0].length()];
        int cont = 0;
        //Anam ficant valors dins l'array de chars
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[0].length; j++) {
                this.mapa[i][j] = correctMap[i].charAt(cont++);
                if (cont == this.mapa[0].length) cont = 0;
            }
        }
        //Cercam on es troba el punt d'interes i el punt de sortida
        this.positionX = getChar('X');
        this.position$ = getChar('$');
    }

    // Navegar fins a l'objectiu («$»).
    // El valor retornat pel mètode consisteix en una cadena de
    // caràcters on cada lletra pot tenir els valors «S», «N», «W» o «E»,
    // segons la posició del robot a cada moment.
    public String run() {
        //Cream un StringBuilder que serà el que retornarem i un char que serà la direcció cap on ens dirigim
        StringBuilder moved = new StringBuilder();
        char actualLetter;
        //Aquí anam comprovant si el mapa conté teletransportadors o iteradors
        if (Arrays.deepToString(this.mapa).contains("T") || Arrays.deepToString(this.mapa).contains("I")) {
            //Cercam les posicions on es troben si en contenen
            int[] positionT = getChar('T');
            int[] positionI = getChar('I');
            //Com que primer hem de anar a per el inversor farem que primer la X es mogui cap l'inversor
            if (positionI[0] > 0) {
                //Fins que no es trobi a la posició de l'inversor el seguira sercant cada cop hem de saber quina
                //direcció ens hem de dirigir
                while (positionI[0] != positionX[0] || positionI[1] != positionX[1]) {
                    actualLetter = whereGo();
                    moved.append(move(positionI, actualLetter));
                }
                //Un cop tenim que ja hem arribat a l'inversor hem d'invertir les prioritats de direcció
                this.iteratorPicked = true;
            }
            //Si tenim teletransportadors
            if (positionT[0] > 0) {
                //Si hem tocat un iterador tendrem més tendència a anar a per un iterador de la part superior del mapa
                if (!iteratorPicked) {
                    //Fins que no arribem a un teletransportador i hem de saber cap on ens hem de dirigir
                    while (positionT[0] != positionX[0] || positionT[1] != positionX[1]) {
                        actualLetter = whereGo();
                        moved.append(move(positionT, actualLetter));
                        //Si es dona el cas de que arriba al $ aturarem
                        if (this.positionX[0] == this.position$[0] && this.positionX[1] == this.position$[1]) break;
                    }
                    //El mateix que antes però aquí si no hem tocat cap inversor hem de fer que vagi a per la T
                    //que es troba a la part més abaix del mapa
                } else {
                    //Fins que no la trobi no la deixarà de sercar seguint sabent cap on es dirigeix
                    while (positionT[2] != positionX[0] || positionT[3] != positionX[1]) {
                        actualLetter = whereGo();
                        moved.append(move(positionT, actualLetter));
                        //Si es dona el cas de que arriba al $ aturarem
                        if (this.positionX[0] == this.position$[0] && this.positionX[1] == this.position$[1]) break;
                    }
                }
            }
        }
        //Finalment aquest serà el while que ens sercarà el $ mirant cap on ens dirigim
        while (this.positionX[0] != this.position$[0] || this.positionX[1] != this.position$[1]) {
            actualLetter = whereGo();
            moved.append(move(position$, actualLetter));
        }
        return moved.toString();
    }

    //Funció que retorna un array de int, serca la posició del mapa on es troba la lletra que volem
    private int[] getChar(char lletra) {
        //Cream l'array que normalment serà de 2 posicions, a no ser, que cerquem T, en aquest cas hem de fer
        //un array de 4 posicions
        int[] charPos = new int[2];
        if (lletra == 'T') charPos = new int[4];
        //Aquí recorrem el mapa fins que trobam la lletra que volem
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[0].length; j++) {
                //Ficarem dins l'array les posicions de la lletra i si es una T hem de cercar l'altre
                if (this.mapa[i][j] == lletra && this.mapa[charPos[0]][charPos[1]] != 'T') {
                    charPos[0] = i;
                    charPos[1] = j;
                    if (lletra == 'T') break;
                    return charPos;
                  //Aquí es on cercam l'altre T
                } else if (this.mapa[i][j] == lletra) {
                    charPos[2] = i;
                    charPos[3] = j;
                    return charPos;
                }
            }
        }
        return charPos;
    }

    //Funció que mourà la X fins que topi amb la paret o trobi el punt d'interes
    private StringBuilder move(int[] lookingFor, char actualLetter) {
        StringBuilder moving = new StringBuilder();
        //Fins que no toquem paret no voldrem aturar de caminar per defecte
        while (touchWall(actualLetter)) {
            //Si es dona el cas que ja hem arribat al $ aturarem
            if (this.positionX[0] == this.position$[0] && this.positionX[1] == this.position$[1]) return moving;
            //Anam sumant la direcció cap on ens dirigim
            moving.append(actualLetter);
            //Feim el calcul depenguent de cap on ens dirigim
            calculation(this.positionX, actualLetter);
            //Aquí el que feim és mirar si hem arribat al transportador i simplement el que feim és canviar les posicions
            //per dir-ho així si esta un T a dalt i l'altre abaix pasariem d'abaix al de adalt o a l'inversa.
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

    //Funció que mira si tocam paret
    private boolean touchWall(char c) {
        //Mirant la direcció cap on ens dirigim comprovam si avançant una posició més o menys depenguent de la direcció
        //estariem tocant paret.
        if (c == 'S') return this.mapa[this.positionX[0] + 1][this.positionX[1]] != '#';
        if (c == 'E') return this.mapa[this.positionX[0]][this.positionX[1] + 1] != '#';
        if (c == 'N') return this.mapa[this.positionX[0] - 1][this.positionX[1]] != '#';
        return this.mapa[this.positionX[0]][this.positionX[1] - 1] != '#';
    }

    //Funció que depenguent de cap on ens dirigim anira sumant la posició de la X
    private int[] calculation(int[] posicio, char lletra) {
        //Sumarà o restarà depenguent de cap on ens estiguem dirigint
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

    //Funció que retorna la direcció cap on ens dirigim
    private char whereGo() {
        //Miram si hem tocat un inversor
        if (!this.iteratorPicked) {
            //Si no l'hem tocat miram cap on podem anar, amb la prioritat per defecte S, E, N, W
            if (this.mapa[this.positionX[0] + 1][this.positionX[1]] != '#') return 'S';
            if (this.mapa[this.positionX[0]][this.positionX[1] + 1] != '#') return 'E';
            if (this.mapa[this.positionX[0] - 1][this.positionX[1]] != '#') return 'N';
            return 'W';
        }
        //Si hem tocat l'inversor, amb l'ordre de prioritat invertit N, W, S, E
        if (this.mapa[this.positionX[0] - 1][this.positionX[1]] != '#') return 'N';
        if (this.mapa[this.positionX[0]][this.positionX[1] - 1] != '#') return 'W';
        if (this.mapa[this.positionX[0] + 1][this.positionX[1]] != '#') return 'S';
        return 'E';
    }

    //Funció que fa un algoritme A*, calcula el mínim de passes que ha de donar el robot per arribar al putn d'interés
    public int bestRun() {
        //Primer de tot cream un mapa de int que sigui com el mapa que tenim.
        int[][] numericMap = new int[this.mapa.length][this.mapa[0].length];
        //Un cop això hem de fer a ma el primer pas, que és, el punt de partida
        //Posam un 1 als costats de la X si no hi ha paret
        if (this.positionX[0] + 1 < this.mapa.length && this.mapa[this.positionX[0] + 1][this.positionX[1]] != '#') numericMap[this.positionX[0] + 1][this.positionX[1]] = 1;
        if (this.positionX[1] + 1 < this.mapa[0].length && this.mapa[this.positionX[0]][this.positionX[1] + 1] != '#') numericMap[this.positionX[0]][this.positionX[1] + 1] = 1;
        if (this.positionX[0] - 1 > 0 && this.mapa[this.positionX[0] - 1][this.positionX[1]] != '#') numericMap[this.positionX[0] - 1][this.positionX[1]] = 1;
        if (this.positionX[1] - 1 > 0 && this.mapa[this.positionX[0]][this.positionX[1] - 1] != '#') numericMap[this.positionX[0]][this.positionX[1] - 1] = 1;
        //Declaram un contador per saber quin número hem de posar
        int cont = 1;
        //Fins que la posició del $ dins el mapa de números estigui buid hem de anar calculant-lo
        while (numericMap[position$[0]][position$[1]] == 0) {
            //Cridam la funció que ens anira posant el número de passes que hem de fer
            editMap(numericMap, cont);
            //Sumant al contador
            cont++;
        }
        return numericMap[position$[0]][position$[1]];
    }

    //Funció que va col·locant al mapa el número de passes que ha de fer per arribar al $
    private void editMap(int[][] numericMap, int contador) {
        //Cream un array per tenir en compte les T
        int[] tes = getChar('T');
        //Anam recorreguent el mapa
        for (int i = 0; i < this.mapa.length; i++) {
            for (int j = 0; j < this.mapa[0].length; j++) {
                //Si trobam un # seguim sercant
                if (i == this.positionX[0] && j == this.positionX[1] || this.mapa[i][j] == '#') continue;
                //Si ens trobam a una T feim el canvi com faría el robot normal i seguim
                if (tes[0] > 0 && numericMap[tes[0]][tes[1]] > 0) {
                    numericMap[tes[2]][tes[3]] = numericMap[i][j];
                    tes[0] = 0;
                } else {
                    //Aquí feim les comprovacions i anam col·locant el número de passes que hem de fer
                    if (i + 1 < this.mapa.length && this.mapa[i][j] != '#' && this.mapa[i + 1][j] != '#' && numericMap[i][j] == contador) numericMap[i + 1][j] = numericMap[i][j] + 1;
                    if (j + 1 < this.mapa[0].length && this.mapa[i][j] != '#' && this.mapa[i][j + 1] != '#' && numericMap[i][j] == contador) numericMap[i][j + 1] = numericMap[i][j] + 1;
                    if (i - 1 > 0 && this.mapa[i][j] != '#' && this.mapa[i - 1][j] != '#' && numericMap[i][j] == contador) numericMap[i - 1][j] = numericMap[i][j] + 1;
                    if (j - 1 > 0 && this.mapa[i][j] != '#' && this.mapa[i][j - 1] != '#' && numericMap[i][j] == contador) numericMap[i][j - 1] = numericMap[i][j] + 1;
                }
            }

        }
    }
}
