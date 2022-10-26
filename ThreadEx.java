import java.awt.Image;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Font;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class ThreadEx extends JFrame{
    
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
        pontuacao=0,    // Para acompanhar o display da pontuuacao
        aux=15,         // Quantidade a ser adicionada para descida
        mov=10;         // Quantidade a ser movida ao clicar nas teclas

    
    boolean exe = true; // Define se está executando ou não (true or false)

    //! Criando objeto
    Image [] imagem = new Image[6];
    ImageIcon [] img = new ImageIcon[6];

    public ThreadEx() {
        //! Adicionando função as teclas direita e esquerda
        addKeyListener((KeyListener) new KeyAdapter(){  
            public void keyPressed(KeyEvent ae){
                int codigo = ae.getKeyCode();
                int setaEsquerda = KeyEvent.VK_LEFT;
                int setaDireita = KeyEvent.VK_RIGHT;
    
                if(codigo==setaEsquerda){
                    mov -= 10;
                    System.out.println("Seta esquerda!");
                }
                if(codigo==setaDireita){
                    mov += 10;
                    System.out.println("Seta direita!");
                }
            }
        });
        
        // Adicionando imagens ao vetor
        for(int i=0; i<=5; i++){
            img[i] = new ImageIcon(i+".png");
            imagem[i] = img[i].getImage();
        }
  
        setSize(400, 500); // Define tamanho do frame
        setVisible(true); // Torna o frame visivel
        setTitle("Game"); // Definindo título pra janela 
        setLocationRelativeTo(null); // Definindo a posição da janela no centro da tela 
        setResizable(false); // Bloqueado maximização e customização de tamanho
        setVisible(true);
        
        showNotify();
        
    }

    public void showNotify(){
        exe = true;
        new Thread(t).start();
    }
           
    public void hideNotify(){
        exe = false;
        t = null;
    }      
    
    public void paint(Graphics g){
        
        if(collide()==false){
            //g.setColor(Color.cyan);
            g.drawImage(imagem[4],0, 0, getWidth(), getHeight(), this);     
            
            //!! Nave
            g.drawImage(imagem[2], mov, getHeight()-130, 80, 90, this);

            if(ybomb1>=500){
                aa = new Random().nextInt(300);
                ybomb1=0;
            }
            if (ybomb2>=500){
                bb = new Random().nextInt(300);
                ybomb2=0;
            }
            if (ybomb3>=500){
                cc = new Random().nextInt(100, 390);
                ybomb3=0;
            }
            g.drawImage(imagem[1], aa, ybomb1+=aux, 40, 40, this);
            g.drawImage(imagem[1], bb, ybomb2+=aux, 40, 40, this);
            g.drawImage(imagem[1], cc, ybomb3+=aux, 40, 40, this);
            
            g.setColor(Color.white);
            g.setFont(new Font("Arial",1, 17));
            g.drawString("Desvie dos obstaculos em 10 segundos", 30, 60);
            g.setFont(new Font("Arial",4, 16));
            pontuacao++;
            g.drawString("Pontuacao: "+ (pontuacao-1), 30, 80);
        }else{
            g.setFont(new Font("Arial Black",1, 20));
            g.setColor(Color.RED);
            g.drawString("GAME OVER", 130, 160);
            g.setColor(Color.yellow);
            g.setFont(new Font("Arial", 4, 16));
            g.drawString("Voce perdeu com "+ (pontuacao-1) + " pontos", 100, 180);
        }
        
        if(tempo_aux%1000 == 0 && collide()==false){
            System.out.println("+ "+tempo_aux);
            seg--;
        }
        g.drawString("Segundos: "+ seg, 30, 100);
        if(seg==0 && collide()==false){
            g.setFont(new Font("Arial Black",1, 18));
            g.setColor(Color.GREEN);
            g.drawString("PARABENS VOCE GANHOU!", 50, 160);
            g.drawImage(imagem[3], 130, 200, 130, 140, this);
            aux=0;
        }
        
    }

    boolean collide() {
        int w1,w2,w3,w4,h1,h2,h3,h4,x1,x2,x3,x4,y1,y2,y3,y4;

        //! Bomba 1
        w1 = 30; // Largura sprite 1
        h1 = 40; // Altura sprite 1
        x1 = aa; // Posição X do sprite 1
        y1 = ybomb1-20; // Posição Y do sprite 1
        //! Bomba 2
        w3 = 30; // Largura sprite 2
        h3 = 40; // Altura sprite 2
        x3 = bb; // Posição X do sprite 2
        y3 = ybomb2-20; // Posição Y do sprite 2
        //! Bomba 3
        w4 = 30; // Largura sprite 3
        h4 = 40; // Altura sprite 3
        x4 = cc; // Posição X do sprite 3
        y4 = ybomb3-20; // Posição Y do sprite 3
        //! Nave
        w2 = 60; // Largura sprite 4
        h2 = 90; // Altura sprite 4
        x2 = mov; // Posição X do sprite 4
        y2 = getHeight()-130; // Posição Y do sprite 4

        if (((x1+w1)>x2) && ((y1+h1)>y2) && ((x2+w2)>x1) && ((y2+h2)>y1)) {
            return true;
        }else if(((x3+w3)>x2) && ((y3+h3)>y2) && ((x2+w2)>x3) && ((y2+h2)>y3)){
            return true;
        }else if(((x4+w4)>x2) && ((y4+h4)>y2) && ((x2+w2)>x4) && ((y2+h2)>y4)){
            return true;
        // }else if(((x1+w1)>x2) && ((y1+h1)>y2) && ((x2+w2)>x1) && ((y2+h2)>y1)){
        //     return true;
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
        ThreadEx d = new ThreadEx();
        d.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}