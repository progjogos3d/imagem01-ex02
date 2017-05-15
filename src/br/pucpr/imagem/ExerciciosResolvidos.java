package br.pucpr.imagem;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ExerciciosResolvidos {
    /**
     * Define uma operação sobre um único pixel, de uma única imagem de entrada
     */
    public interface OperacaoUnaria {
        Vector3 calcular(Vector3 p);
    }

    /**
     * Define uma operação realizada em 2 pixels, de 2 imagens diferentes.
     */
    public interface OperacaoBinaria {
        Vector3 calcular(Vector3 p1, Vector3 p2);
    }

    /**
     * Salva a imagem no disco.
     */
    public void salvar(BufferedImage img, String name) throws IOException {
        ImageIO.write(img, "png", new File(name + ".png"));
        System.out.printf("Salvo %s.png%n", name);
    }

    /**
     * Faz o for sobre a imagem, aplicando a operação unária.
     */
    public BufferedImage filtrar(BufferedImage img, OperacaoUnaria op) {
        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //Lê o pixel
                Vector3 pixel = new Vector3(img.getRGB(x, y));

                //Aplica a operação passada por parâmetro
                Vector3 o = op.calcular(pixel).clamp();

                //Define a cor na imagem de saída
                out.setRGB(x, y, o.getRGB());
            }
        }
        return out;
    }

    /**
     * Faz o for sobre a imagem, aplicando a operação binária.
     */
    public BufferedImage filtrar(BufferedImage img1, BufferedImage img2, OperacaoBinaria op) {

        //Garante que só pixels válidos serão acessados, mesmo que as imagens tenham tamanhos diferentes
        int w = Math.min(img1.getWidth(), img2.getWidth());
        int h = Math.min(img1.getHeight(), img2.getHeight());

        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        //Percorre as imagens
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                //Le os pixels das imagens 1 e 2
                Vector3 p1 = new Vector3(img1.getRGB(x, y));
                Vector3 p2 = new Vector3(img2.getRGB(x, y));

                //Aplica a operação binária passada por parâmetro para calcular a cor de saída
                Vector3 o = op.calcular(p1, p2).clamp();

                //Define a cor na imagem de saída
                out.setRGB(x, y, o.getRGB());
            }
        }
        return out;
    }

    /**
     * Exercício 1
     * Crie uma função BufferedImage bright(BufferedImage img, float intensity)
     * que multiplique todos a cor de todos os pixels da imagem pela intensidade passada.
     * Caso a intensidade seja positiva, deve ser multiplicada diretamente;
     * Caso a intensidade seja negativa, o valor a ser multiplicado é 1 + intensidade. Por exemplo, se a intensidade for
     * -0.2 o valor a ser multiplicado será 0.8.
     *
     * Se o resultado ainda for negativo, use 0 para a intensidade.
     * Se a após a multiplicação do pixel o valor resultante no canal de cor for maior do que 255, grave 255.
     */
    public BufferedImage bright(BufferedImage img, final float intensity) {
        return filtrar(img, p -> p.multiply(intensity < 0 ? 1 + intensity : intensity));
    }

    /**
     * Exercício 2a
     * BufferedImage grayscale(BufferedImage img) que transforme uma imagem colorida em uma imagem em preto e branco.
     * Para isso, defina os valores de r, g e b da imagem de destino iguais ao canal r da imagem de origem
     */
    public BufferedImage grayscaleR(BufferedImage img) {
        return filtrar(img, p -> new Vector3(p.getR(), p.getR(), p.getR()));
    }

    /**
     * Exercício 2b
     * BufferedImage grayscale(BufferedImage img) que transforme uma imagem colorida em uma imagem em preto e branco.
     * Para isso, defina os valores de r, g e b da imagem de destino iguais ao canal r da imagem de origem
     */
    public BufferedImage grayscaleMedia(BufferedImage img) {
        return filtrar(img, p -> {
            float avg = (p.getR() + p.getG() + p.getB()) / 3.0f;
            return new Vector3(avg, avg, avg);
        });
    }

    /**
     * Exercício 2c
     * BufferedImage grayscale(BufferedImage img) que transforme uma imagem colorida em uma imagem em preto e branco.
     * Para isso, defina os valores de r, g e b da imagem de destino iguais a 0.3*r + 0.59*g + 0.11*b da imagem de
     * origem
     */
    public BufferedImage grayscaleFormula(BufferedImage img) {
        return filtrar(img, p -> {
            float avg = p.dot(new Vector3(0.3f, 0.59f, 0.11f));
            return new Vector3(avg, avg, avg);
        });
    }

    /**
     * Exercício 2d
     * Crie a função BufferedImage threshold(BufferedImage img, int value) que pinte de branco todos os pixels
     * maiores ou iguais a value e de preto todos os demais pixels.
     *
     * Considere que img é uma imagem em tons de cinza.
     */
    public BufferedImage threshold(BufferedImage img, final int value) {
        return filtrar(img, p1 -> p1.getR() >= (value / 255.0f) ? new Vector3(Color.WHITE) : new Vector3(Color.BLACK));
    }

    /**
     * Exercício 3a
     * Crie uma função BufferedImage subtract(BufferedImage img1, BufferedImage img2) que receba 2 imagens do mesmo
     * tamanho. Então:
     * Subtraia o pixel (x,y) da imagem 1 do pixel(x,y) da imagem 2;
     * Se o valor resultante no canal de cor for negativo, zere-o;
     */
    public BufferedImage subtract(BufferedImage img1, BufferedImage img2) {
        return filtrar(img1, img2, (p1, p2) -> p1.subtract(p2));
    }

    /**
     * Exercício 3b
     * Faça também uma função add, similar a subtract, mas que soma a cor dos pixels.
     * Novamente, se uma das cores for maior do que 255, force seu valor para 255.
     */
    public BufferedImage add(BufferedImage img1, BufferedImage img2) {
        return filtrar(img1, img2, (p1, p2) -> p1.add(p2));
    }

    /**
     * Exercício 4
     * Faça uma função BufferedImage lerp(BufferedImage img1, BufferedImage img2, float percent) que receba duas
     * imagens de mesmo tamanho e aplique a seguinte fórmula em cada pixel:
     *
     * dst = p1*(1.0f-percent) + p2 * percent
     */
    public BufferedImage lerp(BufferedImage img1, BufferedImage img2, final float percent) {
        return filtrar(img1, img2, (p1, p2) -> p1.multiply(1.0f - percent).add(p2.multiply(percent)));
    }

    /**
     * Exercicio 5
     *
     * Crie a função BufferedImage multiply(BufferedImage img, float[] color) que multiplica cada
     * componente de cor dos pixels de origem pelo componente correspondente da cor passada por parâmetro;
     *
     * Obviamente, essa função deve usar os pixels no intervalo de 0 até 1, como descrito acima.
     * Teste o programa com algumas cores e tente entender o que significa o resultado;
     *
     * Não será necessário criar as funções com floats por o Vector3 já trabalha nesse intervalo
     */
    public BufferedImage multiply(BufferedImage img1, final Vector3 color) {
        return filtrar(img1, p -> p.multiply(color));
    }


    public void run() throws IOException {
        File PATH = new File("/Users/vinigodoy/img");

        //Carrega a imagem da tartaruga
        BufferedImage turtle = ImageIO.read(new File(PATH, "cor/turtle.jpg"));

        //Exercício 1: Brightness. Usa 2 valores. Um dobra o brilho e outro diminui em 50%
        salvar(bright(turtle, 2.0f), "ex1TurtleBright");
        salvar(bright(turtle, -0.5f), "ex1TurtleDark");

        //Exercício 2: Gera 3 tipos de grayscale diferentes e o threshold
        salvar(grayscaleR(turtle), "ex2aTurtleGrayRed");
        salvar(grayscaleMedia(turtle), "ex2bTurtleGrayMedia");
        BufferedImage turtleGray = grayscaleFormula(turtle); //Guardamos para usar no threshold
        salvar(turtleGray, "ex2cTurtleGrayFormula");
        salvar(threshold(turtleGray, 127), "ex2dTurtleTreshold120");

        //Carrega as imagens dos erros para o exercício 3
        BufferedImage erros1 = ImageIO.read(new File(PATH, "pb/errosB1.png"));
        BufferedImage erros2 = ImageIO.read(new File(PATH, "pb/errosB2.png"));

        //Exercício 3a, subtração
        BufferedImage sub1 = subtract(erros1, erros2);
        BufferedImage sub2 = subtract(erros2, erros1);
        salvar(sub1, "ex3aErrosSubtract1");
        salvar(sub2, "ex3aErrosSubtract2");

        //Exercício 3b, soma
        salvar(add(sub1, sub2), "ex3bErrosSoma");

        //Carga das imagens para o exercício 4
        BufferedImage mario = ImageIO.read(new File(PATH, "cor/mario.jpg"));
        BufferedImage sonic = ImageIO.read(new File(PATH, "cor/sonic.jpg"));

        //Aplica a função com os coeficientes solicitados
        for (int i = 1; i < 4; i++) {
            salvar(lerp(mario, sonic, 0.25f * i), "ex4Lerp" + i);
        }

        //Exercicio 5, multiplicação
        salvar(multiply(turtle, new Vector3(1.0f, 0.5f, 0.5f)), "ex5TurtleOnLightRed");
        salvar(multiply(turtle, new Vector3(0.5f, 1.0f, 0.5f)), "ex5TurtleOnLightGreen");
    }

    public static void main(String[] args) throws IOException {
        new ExerciciosResolvidos().run();
    }
}
