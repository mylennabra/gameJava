import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class Jogo extends JFrame{
    
    //! Variáveis de altura das bombas 
    int ybomb1 = 0;
    int ybomb2 = -150;
    int ybomb3 = -450;

    //! Variáveis aleatórias no início, meio e fim
    int aa = new Random().nextInt(100),               // Posição x da primeira bomba
        bb = new Random().nextInt(150, 300),  // Posição x da segunda bomba
        cc = new Random().nextInt(350,400);   // Posição x da terceira bomba

    //! Variáveis do tempo
    int tempo = 100,        // Intervalo a dormir: 100 milisegundos
        tempo_aux = tempo,  // Aux
        seg = 10;           // Para display dos segundos na tela  

    //! Variáveis de movimento
    int a = 0, 
        pontuacao=0,    // Para acompanhar o display da pontuação
        aux=15,         // Quantidade a ser adicionada para descida
        mov=180;         // Quantidade a ser movida ao clicar nas teclas

    boolean exe = true; // Define se está executando ou não (true or false)

    //! Criando objeto
    Image [] imagem = new Image[6];
    ImageIcon [] img = new ImageIcon[6];

    public Jogo() {
        //! Adicionando função as teclas direita e esquerda
        addKeyListener((KeyListener) new KeyAdapter(){  
            public void keyPressed(KeyEvent ae){
                int codigo = ae.getKeyCode();           // Obtendo qual tecla foi apertada
                int setaEsquerda = KeyEvent.VK_LEFT;    // Definindo variável para seta esquerda
                int setaDireita = KeyEvent.VK_RIGHT;    // Definindo variável para seta direita
    
                // Se a tecla apertada for esquerda diminui o valor do eixo x
                if(codigo==setaEsquerda){
                    mov -= 10;
                }
                // Se a tecla apertada for direita aumenta o valor do eixo x
                if(codigo==setaDireita){
                    mov += 10;
                }
            }
        });
        
        // Adicionando imagens ao vetor, cada posição do vetor sera uma imagem
        // Exemplo: img[0] = 0.png
        for(int i=0; i<=5; i++){
            img[i] = new ImageIcon("img\\"+i+".png");   // Definindo o caminho da imagem
            imagem[i] = img[i].getImage();              // Obtendo a imagem encontrada
        }
  
        //! Configurações da janela
        setSize(400, 500);  // Define tamanho do frame
        setVisible(true);               // Torna o frame visivel
        setTitle("Game");           // Definindo título pra janela 
        setLocationRelativeTo(null);    // Definindo a posição da janela no centro da tela 
        setResizable(false);    // Bloqueado maximização e customização de tamanho
        setVisible(true);               // Tornando visível
        
        showNotify(); // Chama o método de iniciar execução da thread
        
    }

    // Inicia a thread e afirma a execução
    public void showNotify(){
        exe = true;
        new Thread(t).start();
    }

    // Para a execução e dá valor nulo a thread
    public void hideNotify(){
        exe = false;
        t = null;
    }      
    
    // Método de "pintar" a tela
    public void paint(Graphics g){
        if(collide()==false){
            // Imagem de fundo
            g.drawImage(imagem[4],0, 0, getWidth(), getHeight(), this);     
            
            // Nave
            g.drawImage(imagem[2], mov, getHeight()-130, 80, 90, this);

            // Se a bomba estiver saindo da tela no chão volta pra cima em outra posição x
            if(ybomb1 >= 500){
                aa = new Random().nextInt(300);
                ybomb1=0;
            }
            if (ybomb2 >= 500){
                bb = new Random().nextInt(300);
                ybomb2=0;
            }
            if (ybomb3 >= 500){
                cc = new Random().nextInt(100, 390);
                ybomb3=0;
            }

            // Bombas
            // A cada execução do repaint a posição y aumentará o valor do aux, sensação de movimento
            g.drawImage(imagem[1], aa, ybomb1+=aux, 40, 40, this);
            g.drawImage(imagem[1], bb, ybomb2+=aux, 40, 40, this);
            g.drawImage(imagem[1], cc, ybomb3+=aux, 40, 40, this);
            
            // Informações escritas ao lado esquerdo superior
            g.setColor(Color.white);
            g.setFont(new Font("Arial",1, 17));
            g.drawString("Desvie dos obstaculos em 10 segundos", 30, 60);
            g.setFont(new Font("Arial",4, 16));

            // Pontuação aumenta 1 a cada execução
            // (pontuacao-1) pois da ultima execução faz mais um acréscimo
            pontuacao++;
            g.drawString("Pontuacao: "+ (pontuacao-1), 30, 80);
        }else{
            // Caso haja colisão irá apresentar mensagem 
            g.setFont(new Font("Arial Black",1, 20));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 130, 160);
            g.setColor(Color.yellow);
            g.setFont(new Font("Arial", 4, 16));
            g.drawString("Voce perdeu com "+ (pontuacao-1) + " pontos", 100, 180);
        }
        
        // Faz a mudança de um segundo a menos sempre que completarem 1000 milissegundos
        // Se o resto da divisão por 1000 for igual a zero então completou mais um segundo
        if(tempo_aux%1000 == 0 && collide()==false){
            seg--;
        }

        // Acompanhamento dos segundos 
        g.drawString("Segundos: "+ seg, 30, 100);

        // Se os segundos acabarem irá parar a execução e apresentar mensagem de vitória
        if(seg==0){
            g.setFont(new Font("Arial Black",1, 18));            // Fonte black
            g.setColor(Color.GREEN);                                              // Cor verde
            g.drawString("PARABENS VOCE GANHOU!", 50, 160);             // Texto
            g.drawImage(imagem[3], 130, 200, 130, 140, this); // Imagem do bob
            hideNotify();
        }
    }

    boolean collide() {
        int w1,w2,w3,w4,h1,h2,h3,h4,x1,x2,x3,x4,y1,y2,y3,y4;

        //! Bomba 1
        w1 = 30;        // Largura sprite 1
        h1 = 40;        // Altura sprite 1
        x1 = aa;        // Posição X do sprite 1
        y1 = ybomb1-20; // Posição Y do sprite 1
        //! Bomba 2
        w2 = 30;        // Largura sprite 2
        h2 = 40;        // Altura sprite 2
        x2 = cc;        // Posição X do sprite 2
        y2 = ybomb3-20; // Posição Y do sprite 2
        //! Bomba 3
        w3 = 30;        // Largura sprite 3
        h3 = 40;        // Altura sprite 3
        x3 = bb;        // Posição X do sprite 3
        y3 = ybomb2-20; // Posição Y do sprite 3
        //! Nave
        w4 = 60;                // Largura sprite 4
        h4 = 50;                // Altura sprite 4
        x4 = mov;               // Posição X do sprite 4
        y4 = getHeight()-130;   // Posição Y do sprite 4

        //! BOMBA1 - x1+w1: A posição mais o tamanho, ou seja, o espaço que A BOMBA ocupa no eixo X, largura
            // ((x1+w1)>x4) : Se a largura ocupada pela bomba é maior que o inicio do X da nave
        //! BOMBA1 - y1+h1: O espaço que ocupa no eixo Y, altura
            // ((y1+h1)>y4) : Se a altura ocupada pela bomba é maior que o inicio do Y da nave
        //! NAVE - x4+w4: O espaço que ocupa no eixo X, largura
            // ((x4+w4)>x1) : Se a altura ocupada pela nave é maior que o inicio do X da bomba
        //! NAVE - y4+h4: O espaço que ocupa no eixo Y, altura
            // ((y4+h4)>y1) : Se a altura ocupada pela nave é maior que o inicio do Y da bomba
        if (((x1+w1)>x4) && ((y1+h1)>y4) && ((x4+w4)>x1) && ((y4+h4)>y1)) {
            return true;
        // Mesmo processo com a bomba 2
        }else if(((x3+w3)>x4) && ((y3+h3)>y4) && ((x4+w4)>x3) && ((y4+h4)>y3)){
            return true;
        // Mesmo processo com a bomba 3
        }else if(((x2+w2)>x4) && ((y2+h2)>y4) && ((x4+w4)>x2) && ((y4+h4)>y2)){
            return true;
        } else {
            return false;
        }
    }

    public Runnable t = new Runnable() {
        public void run() {

            while(exe){
                if(tempo_aux<=10000){
                    try{
                        Thread.sleep(tempo);

                    }catch(Exception e){
                        Thread.currentThread().interrupt();
                    }
                    repaint();
                    tempo_aux += tempo;
                }else{
                    Thread.currentThread().interrupt();
                }
            }
        }
    };


    public static void main(String [] args){
        Jogo d = new Jogo();
        d.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}