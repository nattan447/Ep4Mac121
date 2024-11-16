//vai ter que ser arvore de busca , pois com binary heap com chave sendo a prioridade , na busca por nome ia ficar paia, e não conseguiria buscar em log n por ordem alfabetica
public class Contatinhos {
    // binary search tree dos contatos com a key sendo o nome
    RedblackBSt contatos = new RedblackBSt();
    // binary search tree dos contatos com a key sendo o instagram
    RedblackBSt bstIntagram = new RedblackBSt();
    /**
     * lista os contatos por prioridade
     */
    public void listarPrioridade(){
        Node[] arrayNodes = new Node[contatos.root.size];
        Node[] aux = new Node[arrayNodes.length];
        listarPrioridadeR(contatos.root,arrayNodes,0);
        mergeSort(arrayNodes,aux,0,arrayNodes.length-1);
        for(Node node:arrayNodes){
        printCaracteristicas(node.contato);
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
    Contato contato = new Contato(nome,insta,tele,prioridade);
        if(existeInsta(insta)){
            System.err.println("instagram já existe");
        }else if (existeNome(nome)) System.err.println("nome já cadastrado");
        else {
            contatos.put(contato.nome,contato);
            //   bst com instagram sendo a key principal
            bstIntagram.put(contato.insta,contato);
        }

    }


    /**
     * imprimi a lista de contatos em ordem alfabética dos nomes
     */
    public  void printAlfabetica(){
        printRecursive(contatos.root);
    }

    /**
     * percorre toda arvore , da esquerda para a direira , e printa os seus nós
     * @param root : raiz da arvore
     */
    private  void printRecursive(Node root){
        if(root==null) return;
        printRecursive(root.left);
        printCaracteristicas(root.contato);
        printRecursive(root.right);
    }


    /**
     * busca o contato por nome e por instagram
     * @param nome : nome do contato
     */
    public void buscarContato(String nome){
        Contato busca = contatos.getPnome(nome);
        if(busca==null){
            System.out.println("contato não encontrado");
            return;
        }
        printCaracteristicas(busca);
    }

    /**
     * verifica se o instagram existe na lista de contatos
     * @param insta : nome do instagram
     */
    private boolean existeInsta(String insta){
        Contato busca = bstIntagram.getPnome(insta);
        return busca != null;
    }

    /**
     * verifica se o nome do contato existe na lista de contatos
     * @param  nome : nome do contato
     */
    private boolean existeNome(String nome){
        Contato busca = contatos.getPnome(nome);
        return busca != null;
    }


    /**
     * atualiza os dados de um contato
     * @param nome : nome do contato
     *@param insta : novo instagram
     * @param tele : novo telefone
     * @param prioridade : nova prioridade
     *
     */
    public  void updateContato(String nome, String insta , String tele, int prioridade){
        updateBinary(contatos.root, nome,  insta, tele, prioridade);
    }

    /**
     * faz uma busca binaria pelo nome e atualiza o valor do nó
     * @param nome : nome do contato
     * @param nwinsta : novo telefone
     * @param nwtele : nova prioridade
     * @param nwprioridade : novo instagram
     */
    private void updateBinary(Node x, String nome, String nwinsta, String nwtele, int nwprioridade){
        //essa funcao atualiza os dados do contado
        if(x==null){
            System.out.println("contato não encontrado");
            return;
        }

        int cmp = nome.compareTo(x.nome);

        if (cmp == 0){
            // verifica por uma busca binaria na outra bst se o novo instagram já existe
            if(existeInsta(nwinsta) && !nwinsta.equals(x.contato.insta)){
                System.err.println("falha em atualizar os dados, Instagram já existe");
                return;
            }
            x.contato.insta = nwinsta;
            x.contato.tele = nwtele;
            x.contato.prioridade = nwprioridade;
            return;

        }
        if (cmp < 0)
            updateBinary(x.left, nome, nwinsta,nwtele,nwprioridade);
        else  {
            updateBinary(x.right, nome, nwinsta,nwtele,nwprioridade);
        }

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
        for (int k = lo; k <= hi; k++)
            aux[k] = array[k];
        int i = lo,j  = mid+1;

        for(int k = lo;k<=hi;k++){
            if (i > mid) array[k] = aux[j++];

            else if (j > hi) array[k] = aux[i++];
            else if(aux[i].contato.prioridade>aux[j].contato.prioridade){
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

class RedblackBSt
{   public Node root;
    private static final boolean RED   = true;
    private static final boolean BLACK = false;


    public Contato getPinsta(String nome){
        Node x = root;
        while (x != null)
        {
            int cmp = nome.compareTo(x.nome);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.contato;
        }
        return null;

    }

    public Contato getPnome(String nome)
    {
        Node x = root;
        while (x != null)
        {
            int cmp = nome.compareTo(x.nome);
            if (cmp < 0) x = x.left;
            else if (cmp > 0) x = x.right;
            else return x.contato;
        }
        return null;
    }


    public void put(String nome, Contato cnt)
    { root = put(root, nome, cnt);
        root.color = BLACK;
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

    private Node put(Node x, String nome, Contato cnt)
    {
        if (x == null) return new Node(nome, cnt,RED,1);

        int cmp = nome.compareTo(x.nome);

        if (cmp == 0){
            System.err.println("contato já existe");
            return x;
        }

        if (cmp < 0){
            x.left = put(x.left, nome, cnt);
        }
        else {
            x.right = put(x.right, nome, cnt);
        }

        if (isRed(x.right) && !isRed(x.left))      x = rotateLeft(x);
        if (isRed(x.left)  &&  isRed(x.left.left)) x = rotateRight(x);
        if (isRed(x.left)  &&  isRed(x.right))     flipColors(x);
        x.size = size(x.left) + size(x.right) + 1;

        return  x;

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
        // assert check();
    }


    public boolean contains(String key) {
        return getPinsta(key) != null;
    }

    public boolean isEmpty() {
        return root == null;
    }


    private Node moveRedLeft(Node h) {
        // assert (h != null);
        // assert isRed(h) && !isRed(h.left) && !isRed(h.left.left);

        flipColors(h);
        if (isRed(h.right.left)) {
            h.right = rotateRight(h.right);
            h = rotateLeft(h);
            flipColors(h);
        }
        return h;
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
                h.nome = x.key;
                h.val = x.val;
                // h.val = get(h.right, min(h.right).key);
                // h.key = min(h.right).key;
                h.right = deleteMin(h.right);
            }
            else h.right = delete(h.right, key);
        }
        return balance(h);
    }

}

class Node
{
    public String nome;
    public Contato contato;
    public boolean color;
    public Node left, right;
    public  int size;

    public Node(String  nome, Contato contato, boolean color, int size)
    {
        this.nome= nome;
        this.contato = contato;
        this.color = color;
        this.size = size;

    }
}

