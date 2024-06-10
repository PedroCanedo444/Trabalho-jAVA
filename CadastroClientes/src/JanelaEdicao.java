import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class JanelaEdicao extends JFrame {
    private JTextField cpfField, nomeField;
    private JTextArea comprasArea;
    private Cliente cliente;
    private List<Venda> vendas;

    public JanelaEdicao(Cliente cliente, List<Venda> vendas) {
        this.cliente = cliente;
        this.vendas = vendas;

        setTitle("Edição de Cliente");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        Color backgroundColor = new Color(45, 45, 45);
        Color inputBackgroundColor = new Color(60, 60, 60);
        Color textColor = new Color(255, 255, 255);
        Color buttonColor = new Color(139, 0, 0);
        Color outputBackgroundColor = new Color(220, 220, 220);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        inputPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(inputBackgroundColor);

        Font font = new Font("Arial", Font.PLAIN, 14);

        inputPanel.add(createLabel("CPF:", font, textColor));
        cpfField = createTextField(cliente.getCpf());
        inputPanel.add(cpfField);

        inputPanel.add(createLabel("Nome:", font, textColor));
        nomeField = createTextField(cliente.getNome());
        inputPanel.add(nomeField);

        JButton salvarButton = createButton("Salvar", font, buttonColor, textColor);
        salvarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarAlteracoes();
            }
        });
        inputPanel.add(salvarButton);

        JButton adicionarCompraButton = createButton("Adicionar Compra", font, buttonColor, textColor);
        adicionarCompraButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarNovaCompra();
            }
        });
        inputPanel.add(adicionarCompraButton);

        comprasArea = new JTextArea();
        comprasArea.setEditable(false);
        comprasArea.setBackground(outputBackgroundColor);
        comprasArea.setForeground(Color.BLACK);
        comprasArea.setBorder(new LineBorder(buttonColor, 2));
        mostrarCompras();

        JScrollPane comprasScrollPane = new JScrollPane(comprasArea);
        comprasScrollPane.setBorder(new LineBorder(buttonColor, 2));

        add(inputPanel, BorderLayout.NORTH);
        add(comprasScrollPane, BorderLayout.CENTER);
        getContentPane().setBackground(backgroundColor);
    }

    private JLabel createLabel(String text, Font font, Color textColor) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(textColor);
        return label;
    }

    private JTextField createTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setBackground(Color.WHITE);
        textField.setForeground(Color.BLACK);
        return textField;
    }

    private JButton createButton(String text, Font font, Color backgroundColor, Color textColor) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setBackground(backgroundColor);
        button.setForeground(textColor);
        return button;
    }

    private void salvarAlteracoes() {
        String novoCpf = cpfField.getText().trim();
        String novoNome = nomeField.getText().trim();

        cliente.setCpf(novoCpf);
        cliente.setNome(novoNome);

        JOptionPane.showMessageDialog(this, "Alterações salvas com sucesso.");
    }

    private void mostrarCompras() {
        StringBuilder comprasText = new StringBuilder("Compras:\n\n");
        for (Venda venda : vendas) {
            if (venda.getCliente().getCpf().equals(cliente.getCpf())) {
                comprasText.append("Produto: ").append(venda.getProduto().getNome()).append("\n");
                comprasText.append("Preço: ").append(venda.getProduto().getPreco()).append("\n");
                comprasText.append("Modelo: ").append(venda.getProduto().getModelo()).append("\n");
                comprasText.append("Tamanho: ").append(venda.getProduto().getTamanho()).append("\n\n");
            }
        }
        comprasArea.setText(comprasText.toString());
    }

    private void adicionarNovaCompra() {
        JTextField produtoField = new JTextField();
        JTextField precoField = new JTextField();
        JTextField modeloField = new JTextField();
        JTextField tamanhoField = new JTextField();

        Object[] message = {
                "Produto:", produtoField,
                "Preço:", precoField,
                "Modelo:", modeloField,
                "Tamanho:", tamanhoField,
        };

        int option = JOptionPane.showConfirmDialog(this, message, "Adicionar Nova Compra", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String produto = produtoField.getText().trim();
            double preco;
            try {
                preco = Double.parseDouble(precoField.getText().trim());
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Preço deve ser um número válido.");
                return;
            }
            String modelo = modeloField.getText().trim();
            String tamanho = tamanhoField.getText().trim();

            Produto novoProduto = new Produto(produto, preco, modelo, tamanho);
            Venda novaVenda = new Venda(cliente, novoProduto);
            vendas.add(novaVenda);

            mostrarCompras();
            JOptionPane.showMessageDialog(this, "Nova compra adicionada com sucesso.");
        }
    }
}
