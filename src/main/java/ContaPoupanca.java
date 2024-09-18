public class ContaPoupanca extends Conta {
    public ContaPoupanca(int numero, Cliente dono, double saldo, double limite, String agencia) {
        super(numero, dono, saldo, limite, agencia);
    }

    @Override
    public double calcularTaxas() {
        return 0f;
    }

    @Override
    public void setLimite(double limite) {
        if (limite < 100 || limite > 1000) {
            throw new IllegalArgumentException("Erro: Limite fora da faixa permitida para Conta Poupança.");
        }
        super.limite = limite;
    }

}
