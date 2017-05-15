# imagem01-ex01

Exercícios da aula 1 resolvidos utilizando recursos avançados do java 8.

O aluno deve atentar para o fato de que os fors para percorrer a imagem são sempre os mesmos. O que muda é apenas a 
operação em seu interior:

        //Cria a imagem de saída
        BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

        //Percorre a imagem de entrada
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                //OPERACAO SOBRE A IMAGEM AQUI
            }
        }
        return out;

Essa operação pode ser unária, ou seja, sobre uma única imagem. Ou binária, sobre duas imagens. Para essas, foram 
definidas duas interfaces. 

    public interface OperacaoUnaria {
        Vector3 calcular(Vector3 p);
    }
    
    public interface OperacaoBinaria {
        Vector3 calcular(Vector3 p1, Vector3 p2);
    }
        
Para isso, foram criadas duas funções chamadas filtrar. Estas aplicam as implementações das interfaces das operações.
        
Como cada interface possui uma única função, sua implementação pode ser feita por closures, numa sintaxe muito sucinta.

Por exemplo ao invés de fazer uma classe como:

    public class Negativo implements OperacaoUnaria {
       public Vector3 calcular(Vector3 p) {
            return new Vector3(1, 1, 1).sub(p);
       }
    }

E chamar a função com:

    filtrar(img, new Negativo());
 
É possível simplesmente utilizar a closure fazendo:

    filtrar(img, p -> new Vector3(1, 1, 1).sub(p));
           