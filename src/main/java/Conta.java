import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Conta implements ITaxas, Serializable {

    private static final long serialVersionUID = 1L;

    protected String agencia;

    protected int numero;

    protected Cliente dono;

    protected double saldo;

    protected double limite;

    protected List<Operacao> operacoes;

    protected static int totalContas = 0;

    public Conta(int numero, Cliente dono, double saldo, double limite, String agencia) {
        this.agencia = agencia;
        this.numero = numero;
        this.dono = dono;
        this.saldo = saldo;
        this.limite = limite;

        this.operacoes = new ArrayList<>();

        Conta.totalContas++;
    }

    public void depositar(double valor) throws ValorNegativoException {
        if (valor < 0) {
            throw new ValorNegativoException("Erro: valor negativo para depósito.");
        }
        this.saldo += valor;
        this.operacoes.add(new OperacaoDeposito(valor));
    }

    public boolean sacar(double valor) throws ValorNegativoException, SemLimiteException {
        if (valor < 0) {
            throw new ValorNegativoException("Erro: valor negativo para saque.");
        }
        if (valor > this.limite) {
            throw new SemLimiteException("Erro: limite insuficiente para saque.");
        }
        this.saldo -= valor;
        this.operacoes.add(new OperacaoSaque(valor));
        return true;
    }
    public boolean transferir(Conta destino, double valor) throws ValorNegativoException, SemLimiteException {
        boolean valorSacado = this.sacar(valor);
        if (valorSacado) {
            destino.depositar(valor);
            return true;
        }
        return false;
    }


    @Override
    public String toString() {
        return dono.toString() + '\n' +
                "---\n" +
                "numero=" + numero + '\n' +
                "saldo=" + saldo + '\n' +
                "limite=" + limite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o instanceof Conta) {
            Conta conta = (Conta) o;
            return numero == conta.numero;
        }
        return false;
    }

    public void imprimirExtrato(int modo) {
        // realiza a cópia para não perder a ordem original da lista
        List<Operacao> operacoesParaExtrato = new ArrayList<>(this.operacoes);

        // Ordena de modo == 1, caso contrário mantém ordem original
        if (modo == 1) {
            Collections.sort(operacoesParaExtrato);
        }

        System.out.println("======= Extrato Conta " + this.numero + "======");
        for(Operacao atual : operacoesParaExtrato) {
            System.out.println(atual);
        }
        System.out.println("====================");
    }

    public void imprimirExtratoTaxas() {
        System.out.println("=== Extrato de Taxas ===");
        System.out.printf("Manutenção:\t%.2f\n", this.calcularTaxas());

        double totalTaxas = this.calcularTaxas();
        for (Operacao atual : this.operacoes) {
            totalTaxas += atual.calcularTaxas();
            System.out.printf("%c:\t%.2f\n", atual.getTipo(), atual.calcularTaxas());
        }

        System.out.printf("Total:\t%.2f\n", totalTaxas);
    }

    public void salvar() throws IOException{
        File diretorio = new File("DadosContas");
        if (!diretorio.exists()) {
            diretorio.mkdir();
        }

        String fname = this.agencia + "-" + this.numero + ".ser";
        FileOutputStream contaFile = new FileOutputStream("DadosContas/" + fname);

        ObjectOutputStream contaSer = new ObjectOutputStream(contaFile);

        contaSer.writeObject(this);

        contaSer.flush();
        contaSer.close();
    }


    static public Conta carregar(String agencia, int numeroConta) throws IOException, ClassNotFoundException{
        String fname = agencia + "-" + numeroConta + ".ser";
        FileInputStream contaFile = new FileInputStream("DadosContas/" + fname);
        ObjectInputStream contaSer = new ObjectInputStream(contaFile);

        Conta conta = (Conta) contaSer.readObject();

        contaSer.close();

        return conta;
    }

    public int getNumero() {
        return numero;
    }

    public Cliente getDono() {
        return dono;
    }

    public double getSaldo() {
        return saldo;
    }

    public double getLimite() {
        return limite;
    }

    public static int getTotalContas() {
        return Conta.totalContas;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public void setDono(Cliente dono) {
        this.dono = dono;
    }

    public abstract void setLimite(double limite);
}
