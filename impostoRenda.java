package impostoRenda;

import java.util.Scanner;
import java.util.regex.Pattern;

public class impostoRenda {

    private static final double ISENTO = 0.0;
    private static final double IMPOSTO_10 = 0.10;
    private static final double IMPOSTO_20 = 0.20;
    private static final double IMP_SERVICO = 0.15;
    private static final double GANHO_CAPITAL = 0.20;
    private static final double ABATE_MAXIMO = 0.30;

    private static final String NUMERO_REGEX = "^[0-9]+(\\.[0-9]{1,2})?$"; // número com até duas casas decimais

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("===================================");
        System.out.println("=== Cálculo de Imposto de Renda ===");
        System.out.println("===================================\n");
        System.out.println("ATENÇÃO!!\nNo lugar de ,(vírgula) utilize .(ponto) exemplo: 0.00\n");

        double rendaSalarioAnual = lerValor(scanner, " - Informe a renda anual com salário: ");
        double rendaServicoAnual = lerValor(scanner, " - Informe a renda anual com prestação de serviços: ");
        double rendaGanhoCapitalAnual = lerValor(scanner, " - Informe a renda anual com ganho de capital: ");
        double gastosMedicinais = lerValor(scanner, " - Informe os gastos médicos: ");
        double gastosEducacionais = lerValor(scanner, " - Informe os gastos educacionais: ");

        double impostoSalario = calcularImpostoSalario(rendaSalarioAnual);
        double impostoServico = calcularImpostoServico(rendaServicoAnual);
        double impostoGanhoCapital = calcularImpostoGanhoCapital(rendaGanhoCapitalAnual);

        double impostoBruto = impostoSalario + impostoServico + impostoGanhoCapital;
        double gastosDedutiveis = gastosMedicinais + gastosEducacionais;
        double abate = calcularAbate(gastosDedutiveis, impostoBruto);
        double impostoDevido = impostoBruto - abate;

        gerarRelatorio(rendaSalarioAnual, rendaServicoAnual, rendaGanhoCapitalAnual, 
                       gastosDedutiveis, impostoBruto, abate, impostoDevido);

        scanner.close();
    }

    private static double lerValor(Scanner scanner, String mensagem) {
        String valorString;
        double valor = -1;
        while (valor < 0) {
            System.out.print(mensagem);
            valorString = scanner.nextLine();
            if (Pattern.matches(NUMERO_REGEX, valorString)) {
                valor = Double.parseDouble(valorString);
                if (valor < 0) {
                    System.out.println("\nDigite apenas números positivos com até duas casas decimais.");
                }
            } else {
                System.out.println("\nDigite apenas números positivos com até duas casas decimais.");
            }
        }
        return valor;
    }

    private static double calcularImpostoSalario(double rendaAnual) {
        if (rendaAnual < 36000) {
            return ISENTO;
        } else if (rendaAnual < 60000) {
            return rendaAnual * IMPOSTO_10;
        } else {
            return rendaAnual * IMPOSTO_20;
        }
    }

    private static double calcularImpostoServico(double rendaAnual) {
        return (rendaAnual > 0) ? (rendaAnual * IMP_SERVICO) : 0;
    }

    private static double calcularImpostoGanhoCapital(double rendaAnual) {
        return (rendaAnual > 0) ? (rendaAnual * GANHO_CAPITAL) : 0;
    }

    private static double calcularAbate(double gastos, double impostoBruto) {
        double abateMaximo = impostoBruto * ABATE_MAXIMO;
        return Math.min(gastos, abateMaximo);
    }

    private static void gerarRelatorio(double rendaSalario, double rendaServico, double rendaGanhoCapital, 
    double gastosDedutiveis, double impostoBruto, double abate, double impostoDevido) {

        System.out.println("\n ___________________________________\n");
        System.out.println("|   RELATÓRIO DO IMPOSTO DE RENDA   |");
        System.out.println(" ___________________________________\n");

        System.out.println("- Consolidado de renda:");
        System.out.printf("  Imposto sobre salário: R$ %.2f\n", calcularImpostoSalario(rendaSalario));
        System.out.printf("  Imposto sobre serviços: R$ %.2f\n", calcularImpostoServico(rendaServico));
        System.out.printf("  Imposto sobre ganho de capital: R$ %.2f\n", calcularImpostoGanhoCapital(rendaGanhoCapital));

        System.out.println("\n- Deduções:");
        double maxDedutivel = impostoBruto * ABATE_MAXIMO;
        System.out.printf("  Máximo dedutível: R$ %.2f\n", maxDedutivel);
        System.out.printf("  Gastos dedutíveis: R$ %.2f\n", gastosDedutiveis);

        System.out.println("\n- Resumo:");
        System.out.printf("  Imposto bruto total: R$ %.2f\n", impostoBruto);
        System.out.printf("  Abatimento: R$ %.2f\n", abate);
        System.out.printf("  Imposto devido: R$ %.2f\n", impostoDevido);
    }
}
