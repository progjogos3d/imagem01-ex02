# Aula 1 - Exercícios Resolvidos (Avançado)

Exercícios da aula 1 resolvidos utilizando recursos avançados do java 8. Uma resolução mais simples pode ser encontrada
no repositório [imagem01-ex01](https://github.com/progjogos3d/imagem01-ex01).

## Vector3
A primeira melhoria é utilizar uma classe para representar cores na forma de um vetor matemático. Trata-se da classe 
```Vector3```. Essa classe já implementa algumas operações comuns para cores como a multiplicação 
componente-a-componente, saturação (clamp), um construtor que aceita a cor na forma de um int ou Color e um construtor 
para gerar a cor na forma de um ```int```. Assim, operações como subtração e multiplicação passam a ser operações 
vetoriais, e não precisam estar explicitamente realizadas canal-a-canal. Por exemplo, para multilpicar a cor por um 
escalar ao invés de:

```java
int r = p.getRed() * escalar;
int g = p.getGreen() * escalar;
int b = p.getBlue() * escalar;
```
Pode-se usar simplesmente:
```java
cor.multiply(escalar);
```

##Estruturação do código

O aluno deve atentar para o fato de que os fors para percorrer a imagem são sempre os 
mesmos. O que muda é apenas a operação em seu interior:

```java
//Cria a imagem de saída
BufferedImage out = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);

//Percorre a imagem de entrada
for (int y = 0; y < img.getHeight(); y++) {
    for (int x = 0; x < img.getWidth(); x++) {
        //OPERACAO SOBRE A IMAGEM AQUI
    }
}
return out;
```

Essa operação pode ser unária, ou seja, sobre uma única imagem. Ou binária, sobre duas imagens. Para essas, foram 
definidas duas interfaces. 

```java
public interface OperacaoUnaria {
    Vector3 calcular(Vector3 p);
}

public interface OperacaoBinaria {
    Vector3 calcular(Vector3 p1, Vector3 p2);
}
```
        
Para isso, foram criadas duas funções chamadas filtrar. Estas aplicam as implementações das interfaces das operações.

## Lambda (Java 8)        
Como cada interface possui uma única função, sua implementação pode ser feita por closures, numa sintaxe muito sucinta.

Por exemplo ao invés de fazer uma classe como:

```java
public class Negativo implements OperacaoUnaria {
   public Vector3 calcular(Vector3 p) {
        return new Vector3(1, 1, 1).sub(p);
   }
}
```

E chamar a função com:

```java
filtrar(img, new Negativo());
```
 
É possível simplesmente utilizar a closure fazendo:

```java
filtrar(img, p -> new Vector3(1, 1, 1).sub(p));
```           