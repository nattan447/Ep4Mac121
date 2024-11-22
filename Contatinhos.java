/***************************************************************************
 * MAC0121 ALGORITMOS E ESTRUTURAS DE DADOS I
 * Nome: [nattan ferreira da silva]
 * Número USP: [15520641]
 * Exercício-Programa: [EP4]
 * [implementar estruturas de dados em um sistema de gerenciamentps de contastos]
 *
 ***************************************************************************/


import java.util.NoSuchElementException;

public class Contatinhos {

    // simbol table dos contatos com a key sendo o nome
    private final RedblackBSt STnome = new RedblackBSt();
    // simbol table dos contatos com a key sendo o instagram
    private final RedblackBSt STinstagram = new RedblackBSt();
    /**
     * lista os contatos por prioridade
     */
    public void listarPrioridade(){
        //colocaremos todos contastos em arrayNodes
        Node[] arrayNodes = new Node[STnome.root.size];
        Node[] aux = new Node[arrayNodes.length];
        listarPrioridadeR(STnome.root,arrayNodes,0);
        //depois que todos contatos estao no array ordenamos por prioridade usando o mergeSort
        mergeSort(arrayNodes,aux,0,arrayNodes.length-1);
        for(Node node:arrayNodes){
        printCaracteristicas(node.val);
        }

    }
    /**
     * adiciona todos nós da arvore em um array
     * @param root : raiz da arvore
     * @param index : começo do array
     * @param arrayNodes : array que guardara os nós
     * @return index atual do array
     */
    private int listarPrioridadeR(Node root,Node[] arrayNodes,int index) {
        //essa função percorre todos nós e os coloca em um array
        if(root==null) return index;
        //a função retorna um inteiro para guardar o index que foi incrementado nas chamadas recursivas internas , conseguindo colocar cada contato em um espaço de array diferente
        index = listarPrioridadeR(root.left,arrayNodes,index);
        arrayNodes[index++]=root;
        index = listarPrioridadeR(root.right,arrayNodes,index);
        return  index;
    }
    /**

     * adiciona o contato nas 'lista' de contatos
     * @param nome : nome do contato
     *@param insta : instagram do contato
     * @param tele : telefone do contato
     * @param prioridade : prioridade do contato
     */
    public  void addContato(String nome, String insta , String tele, int prioridade){
        if(nome==null || insta==null || tele==null){
            System.err.println("ERRO: dados insuficientes");
            return;
        }
        //contato que iremos adicionar
        Contato contato = new Contato(nome,insta,tele,prioridade);
        if (primeiroContato==null) primeiroContato=contato;

        if(existeInsta(insta)){
            System.err.println("ERRO AO ADICIONAR O CONTATO : INSTAGRAM : "+ insta + " JÁ EXISTE");
        }else if (existeNome(nome)) System.err.println("ERRO AO ADICIONAR O CONTATO : NOME : "+ nome + " JÁ EXISTE");
        else {
            STnome.put(contato.nome,contato);
            //   Symbol table com instagram sendo a key principal
            STinstagram.put(contato.insta,contato);
        }

    }
    /**
     * imprimi a lista de contatos em ordem alfabética dos nomes
     */
    public  void printAlfabetica(){
        printRecursive(STnome.root);
    }

    /**
     * percorre toda arvore , da esquerda para a direira , e printa os seus nós
     * @param root : raiz da arvore
     */
    private  void printRecursive(Node root){
        if(root==null) return;
        printRecursive(root.left);
        printCaracteristicas(root.val);
        printRecursive(root.right);

    }

    /**
     * busca o contato por nome ou por instagram
     * @param key : nome do contato ou perfil do instagram
     */
    public void buscarContato(String key){
        Contato buscaPorNome = STnome.get(key);
        Contato buscaPorInsta = STinstagram.get(key);
        if(buscaPorNome == null && buscaPorInsta==null) {
            System.err.println("CONTATO  "+key+ "   NÃO ENCONTRADO");
            return;
        }
        if(buscaPorInsta!=null){
            printCaracteristicas(buscaPorInsta);
        } else  {
            printCaracteristicas(buscaPorNome);
        }
    }

    /**
     * verifica se o instagram existe na lista de contatos
     * @param insta : nome do instagram
     */
    private boolean existeInsta(String insta){
        Contato busca = STinstagram.get(insta);
        return busca != null;
    }

    /**
     * verifica se o nome do contato existe na lista de contatos
     * @param  nome : nome do contato
     */
    private boolean existeNome(String nome){
        Contato busca = STnome.get(nome);
        return busca != null;
    }
    /**
     * atualiza os dados de um contato, com base no nome desse contato
     * @param key : nome ou instragram do contato que queremos atualizar
     *@param insta : novo instagram
     * @param tele : novo telefone
     * @param prioridade : nova prioridade
     *
     */
    public  void atualizarContato(String key, String insta , String tele, int prioridade){
        // verificamos se o usário quer atualizar o contato por instagram
        Contato buscaPorInsta = STinstagram.get(key);
        if(buscaPorInsta==null){
            //se não querer , então é por nome
            updateBinary(STnome.root, key,  insta, tele, prioridade);
        }else  {
            // se querer buscamos por instagram e dps pegamos o nome do contato
             key = buscaPorInsta.nome;
            updateBinary(STnome.root, key,  insta, tele, prioridade);
        }


    }

    /**
     * faz uma busca binaria pelo nome e atualiza o valor do nó
     * @param nome : nome do contato
     * @param nwtele : nova prioridade
     * @param nwinsta : novo telefone
     * @param nwprioridade : novo instagram
     */
    private void updateBinary(Node root, String nome, String nwinsta, String nwtele, int nwprioridade){
        //essa funcao atualiza os dados do contado
        if(root==null){
            System.err.println("ERROR : CONTATO COM NOME : "+ nome + "NÃO ENCONTRADO");
            return;
        }

        int cmp = nome.compareTo(root.key);

        if (cmp == 0){
            // verifica por uma busca binaria na outra bst se o novo instagram já existe
            if(existeInsta(nwinsta) && !nwinsta.equals(root.val.insta)){
                System.err.println("FALHA EM ATUALIZAR OS DADOS , INSTAGRAM COM NOME "+nwinsta+ " JÁ EXISTE");
                return;
            }

            //deleto o nó antigo , adiciono um novo nó na arvore do insta , para , se necessário alterear o valor do instagram
            STinstagram.delete(root.val.insta); //insta antigo
            STinstagram.put(nwinsta, new Contato(nome,nwinsta,nwtele,nwprioridade));
            root.val.insta = nwinsta;
            root.val.tele = nwtele;
            root.val.prioridade = nwprioridade;
            return;

        }
        if (cmp < 0)
            updateBinary(root.left, nome, nwinsta,nwtele,nwprioridade);
        else  {
            updateBinary(root.right, nome, nwinsta,nwtele,nwprioridade);
        }

    }


    //unica funcao dessa var: guardar o primeiro contato adicionado
    private Contato primeiroContato = null;
    // essa variavel servira de base para a funcao remover
    private  boolean jadeletou = false;
    /**
     * remove o primeiro contato adicionado na lista de contatos
     *
     */
public void remover(){
    if(jadeletou){
        System.err.println("ERRO: PRIMEIRO CONTATO ADICIONADO JÁ REMOVIDO");
        return;
    }
    STnome.delete(primeiroContato.nome);
    STinstagram.delete(primeiroContato.insta);
    jadeletou= true;
    System.out.println("primeiro contato removido com sucesso");
}

    /**
     * printa todas variaveis de um contato
     * @param contato : contato que queremos trabalhar
     */
    private void printCaracteristicas(Contato contato){
        System.out.println("nome : "+contato.nome);
        System.out.println("instagram : "+contato.insta);
        System.out.println("telefone : "+contato.tele);
        System.out.println("prioridade : "+contato.prioridade);
        System.out.println(" ");
    }



    //MERGESORT
    private   static  void mergeSort(Node[] array,Node[] aux,int lo , int hi){
        if(hi<=lo) return;
        int mid = lo + (hi - lo) / 2;
        mergeSort(array, aux, lo, mid);
        mergeSort(array, aux, mid+1, hi);
        merge(array, aux, lo, mid, hi);

    }

    private static void merge(Node[] array,Node[] aux, int lo,int mid,int hi){
        for(int k = lo; k <= hi; k++)
            aux[k] = array[k];
        int i = lo,j  = mid+1;
        for(int k = lo;k<=hi;k++){
            if (i > mid) array[k] = aux[j++];

            else if (j > hi) array[k] = aux[i++];
            else if(aux[i].val.prioridade>aux[j].val.prioridade){
                array[k] =aux[j++];
            }else {
                array[k]=aux[i++];
            }
        }
    }
}

class  Contato{
    public String nome,insta,tele;
    public int prioridade;

    public  Contato(String nome, String insta , String tele, int prioridade){
        this.nome=nome;
        this.insta=insta;
        this.tele=tele;
        this.prioridade=prioridade;

    }
}



///////////////ESTRUTURA DE DADO, ARVORE BINÁRIA DE BUSCA RED BLACK , OBS : ALGUMAS OPERAÇÕES FORAM RETIRADAS PELA NÃO NECESSIDADE DE USO , EX ; rank() , isBalanced
///APENAS AS OPERAÇÕES PADROES EX : GET , PUT , DELETE e a operacao de de balanceamento da arvore foram mantidas
///como a operacao de balanceamento foi mantida , logo , é de crer que a arvore esteja balanceada.


class Node
{
    public String key;
    public Contato val;
    public boolean color;
    public Node left, right;
    public  int size;

    public Node(String  nome, Contato contato, boolean color, int size)
    {
        this.key= nome;
        this.val = contato;
        this.color = color;
        this.size = size;

    }
}


class RedblackBSt
{   public Node root;
    private static final boolean RED   = true;
    private static final boolean BLACK = false;



    public Contato get(String key) {
        if (key == null) throw new IllegalArgumentException("argument to get() is null");
        return get(root, key);
    }

    private Contato get(Node x, String key) {
        while (x != null) {
            int cmp = key.toLowerCase().compareTo(x.key.toLowerCase());
            if      (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else              return x.val;
        }
        return null;
    }


    /**
     * Inserts the specified key-value pair into the symbol table, overwriting the old
     * value with the new value if the symbol table already contains the specified key.
     * Deletes the specified key (and its associated value) from this symbol table
     * if the specified value is {@code null}.
     *
     * @param key the key
     * @param val the value
     * @throws IllegalArgumentException if {@code key} is {@code null}
*/
    public void put(String key, Contato val)
    {    if (key == null) throw new IllegalArgumentException("first argument to put() is null");
        if (val == null) {
            delete(key);
            return;
        }
        root = put(root, key, val);
        root.color = BLACK;
    }




    private Node put(Node x, String key, Contato val)
    {
        if (x == null) return new Node(key, val,RED,1);

        int cmp = key.compareTo(x.key);
        //OBS , auteracao na estrutura original , se o o contato que queremso adcionar existe , nao adcionamos nada
        if (cmp == 0){
            System.err.println("contato já existe");
            return x;
        }
       else if (cmp < 0){
            x.left = put(x.left, key, val);
        }
        else {
            x.right = put(x.right, key, val);
        }
        if (isRed(x.right) && !isRed(x.left))      x = rotateLeft(x);

        if (isRed(x.left)  &&  isRed(x.left.left)) x = rotateRight(x);
        if (isRed(x.left)  &&  isRed(x.right))     flipColors(x);
        x.size = size(x.left) + size(x.right) + 1;

        return  x;

    }

    private boolean isRed(Node x) {
        if (x == null) return false;
        return x.color == RED;
    }
    private void flipColors(Node h) {
        h.color = !h.color;
        h.left.color = !h.left.color;
        h.right.color = !h.right.color;
    }


    private Node rotateRight(Node h) {
        assert (h != null) && isRed(h.left);
        Node x = h.left;
        h.left = x.right;
        x.right = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }

    private Node rotateLeft(Node h) {
        assert (h != null) && isRed(h.right);
        // assert (h != null) && isRed(h.right) && !isRed(h.left);  // for insertion only
        Node x = h.right;
        h.right = x.left;
        x.left = h;
        x.color = h.color;
        h.color = RED;
        x.size = h.size;
        h.size = size(h.left) + size(h.right) + 1;
        return x;
    }


    private int size(Node x) {
        if (x == null) return 0;
        return x.size;
    }


    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return {@code true} if this symbol table contains {@code key} and
     *     {@code false} otherwise
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public boolean contains(String key) {
        return get(key) != null;
    }


    /**
     * Removes the specified key and its associated value from this symbol table
     * (if the key is in this symbol table).
     *
     * @param  key the key
     * @throws IllegalArgumentException if {@code key} is {@code null}
     */
    public void delete(String key) {
        if (key == null) throw new IllegalArgumentException("argument to delete() is null");
        if (!contains(key)) return;

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = delete(root, key);
        if (!isEmpty()) root.color = BLACK;

    }



    // delete the key-value pair with the given key rooted at h
    private Node delete(Node h, String key) {
        // assert get(h, key) != null;

        if (key.compareTo(h.key) < 0)  {
            if (!isRed(h.left) && !isRed(h.left.left))
                h = moveRedLeft(h);
            h.left = delete(h.left, key);
        }
        else {
            if (isRed(h.left))
                h = rotateRight(h);
            if (key.compareTo(h.key) == 0 && (h.right == null))
                return null;
            if (!isRed(h.right) && !isRed(h.right.left))
                h = moveRedRight(h);
            if (key.compareTo(h.key) == 0) {
                Node x = min(h.right);
                h.key = x.key;
                h.val = x.val;
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            }
            else h.right = delete(h.right, key);
        }
        return balance(h);
    }
    /**
     * Returns the smallest key in the symbol table.
     * @return the smallest key in the symbol table
     * @throws NoSuchElementException if the symbol table is empty
     */
    public String min() {
        if (isEmpty()) throw new NoSuchElementException("calls min() with empty symbol table");
        return min(root).key;
    }

    // the smallest key in subtree rooted at x; null if no such key
    private Node min(Node x) {
        // assert x != null;
        if (x.left == null) return x;
        else                return min(x.left);
    }


    /**
     * Removes the smallest key and associated value from the symbol table.
     * @throws NoSuchElementException if the symbol table is empty
     */
    public void deleteMin() {
        if (isEmpty()) throw new NoSuchElementException("BST underflow");

        // if both children of root are black, set root to red
        if (!isRed(root.left) && !isRed(root.right))
            root.color = RED;

        root = deleteMin(root);
        if (!isEmpty()) root.color = BLACK;
        // assert check();
    }

    // delete the key-value pair with the minimum key rooted at h
    private Node deleteMin(Node h) {
        if (h.left == null)
            return null;

        if (!isRed(h.left) && !isRed(h.left.left))
            h = moveRedLeft(h);

        h.left = deleteMin(h.left);
        return balance(h);
    }
    /**
     * Is this symbol table empty?
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }
    // restore red-black tree invariant
    private Node balance(Node h) {
         assert (h != null);
        if (isRed(h.right) && !isRed(h.left))    h = rotateLeft(h);
        if (isRed(h.left) && isRed(h.left.left)) h = rotateRight(h);
        if (isRed(h.left) && isRed(h.right))     flipColors(h);

        h.size = size(h.left) + size(h.right) + 1;
        return h;
    }
    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private Node moveRedLeft(Node h) {
        assert (h != null);
         assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);
        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
    }

            // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private Node moveRedRight(Node h) {
         assert (h != null);
         assert isRed(h) && !isRed(h.right) && !isRed(h.right.left);
        flipColors(h);
        if (isRed(h.left.left)) {
            h = rotateRight(h);
            flipColors(h);
        }
        return h;
    }

}



