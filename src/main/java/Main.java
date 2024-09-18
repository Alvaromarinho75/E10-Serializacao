import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner entrada = new Scanner(System.in);

        Cliente joao = new PessoaFisica("João", "Av. Antonio Carlos, 6627",
                new Date(), "111.111.111-11", 36, 'm');
        Cliente lojinha = new PessoaJuridica("Padaria", "Nacoes Unidas",
                new Date(), "000.00000.0000/0001", 35, "Comércio");

        Conta cc = new ContaCorrente(1234, joao, 0, 1500, "10.507-11");
        Conta cp = new ContaPoupanca(4321, lojinha, 10000, 1500, "10.507-11");

        try {
            cc.salvar();
        } catch (Exception v) {
            System.out.println(v);
        }

        try {
            Conta contaCarregada = Conta.carregar(cc.agencia, 1234);
            System.out.println(contaCarregada);
        }catch(FileNotFoundException e){
            System.out.println("Conta nao encontrada! Agencia: " + cc.agencia + " ;N da conta: " + cc.getNumero());
        }catch(ClassNotFoundException e){
            System.out.println("Conflito na versao das classes!");
        }catch(IOException e){
            System.out.println(e);
        }
    }
}
