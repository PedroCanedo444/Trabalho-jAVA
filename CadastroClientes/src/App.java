import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class App extends JFrame {
    private JTextField cpfField, nomeField, produtoField, precoField, modeloField, tamanhoField, searchField;
    private JButton cadastrarButton, listarButton, searchButton;
    private JTextArea outputArea;
    private JPanel inputPanel, searchPanel, resultPanel;
    private List<Venda> vendas;
    private Set<String> nomesClientes;

    public App() {
        vendas = new ArrayList<>();
        nomesClientes = new HashSet<>();

        setTitle("Cadastro de Produtos");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        Color backgroundColor = new Color(45, 45, 45);
        Color inputBackgroundColor = new Color(60, 60, 60);
        Color textColor = new Color(255, 255, 255);
        Color buttonColor = new Color(139, 0, 0);
        Color outputBackgroundColor = new Color(220, 220, 220);

        inputPanel = new JPanel(new GridLayout(8, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setBackground(inputBackgroundColor);

        Font font = new Font("Arial", Font.PLAIN, 14);

        inputPanel.add(createLabel("CPF do Cliente:", font, textColor));
        cpfField = createTextField();
        inputPanel.add(cpfField);

        inputPanel.add(createLabel("Nome do Cliente:", font, textColor));
        nomeField = createTextField();
        inputPanel.add(nomeField);

        inputPanel.add(createLabel("Produto:", font, textColor));
        produtoField = createTextField();
        inputPanel.add(produtoField);

        inputPanel.add(createLabel("Preço:", font, textColor));
        precoField = createTextField();
        inputPanel.add(precoField);

        inputPanel.add(createLabel("Modelo:", font, textColor));
        modeloField = createTextField();
        inputPanel.add(modeloField);

        inputPanel.add(createLabel("Tamanho:", font, textColor));
        tamanhoField = createTextField();
        inputPanel.add(tamanhoField);

        cadastrarButton = createButton("Cadastrar Venda", font, buttonColor, textColor);
        cadastrarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cadastrarVenda();
            }
        });
        inputPanel.add(cadastrarButton);

        listarButton = createButton("Listar Clientes", font, buttonColor, textColor);
        listarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                listarNomesClientes();
            }
        });
        inputPanel.add(listarButton);

        searchPanel = new JPanel(new BorderLayout(10, 10));
        searchPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        searchPanel.setBackground(backgroundColor);
        searchPanel.setVisible(true);

        searchField = createTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchButton = createButton("Pesquisar", font, buttonColor, textColor);
        searchButton.setPreferredSize(new Dimension(100, 30));
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pesquisarNomeCliente();
            }
        });

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        resultPanel = new JPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(outputBackgroundColor);

        JScrollPane resultScrollPane = new JScrollPane(resultPanel);
        resultScrollPane.setBorder(new LineBorder(buttonColor, 2));

        add(searchPanel, BorderLayout.NORTH);
        add(resultScrollPane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        getContentPane().setBackground(backgroundColor);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setBackground(outputBackgroundColor);
        outputArea.setForeground(Color.BLACK);
        outputArea.setBorder(new LineBorder(buttonColor, 2));
        resultPanel.add(new JScrollPane(outputArea));
    }

    private JLabel createLabel(String text, Font font, Color textColor) {
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setForeground(textColor);
        return label;
    }

    private JTextField createTextField() {
        JTextField textField = new JTextField();
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

    private void cadastrarVenda() {
        String cpf = cpfField.getText().trim();
        String nome = nomeField.getText().trim();
        String produto = produtoField.getText().trim();
        String precoText = precoField.getText().trim();
        String modelo = modeloField.getText().trim();
        String tamanho = tamanhoField.getText().trim();

        double preco;
        try {
            preco = Double.parseDouble(precoText);
        } catch (NumberFormatException e) {
            mostrarResultado("Preço deve ser um número válido.");
            return;
        }

        if (nomesClientes.contains(nome)) {
            mostrarResultado("Cliente já cadastrado.");
            return;
        }

        Cliente cliente = new Cliente(cpf, nome);
        nomesClientes.add(nome);

        Produto prod = new Produto(produto, preco, modelo, tamanho);
        Venda venda = new Venda(cliente, prod);
        vendas.add(venda);

        StringBuilder mensagem = new StringBuilder();
        mensagem.append("Venda cadastrada com sucesso:\n\n");
        mensagem.append("Cliente:\n");
        mensagem.append("CPF: ").append(cliente.getCpf()).append("\n");
        mensagem.append("Nome: ").append(cliente.getNome()).append("\n\n");
        mensagem.append("Produto:\n");
        mensagem.append("Nome: ").append(prod.getNome()).append("\n");
        mensagem.append("Preço: ").append(prod.getPreco()).append("\n");
        mensagem.append("Modelo: ").append(prod.getModelo()).append("\n");
        mensagem.append("Tamanho: ").append(prod.getTamanho()).append("\n");
        mostrarResultado(mensagem.toString());

        limparCampos();
    }

    private void listarNomesClientes() {
        resultPanel.removeAll();
        nomesClientes.clear(); // Limpar o conjunto de nomes de clientes

        for (Venda venda : vendas) {
            String nomeCliente = venda.getCliente().getNome();
            nomesClientes.add(nomeCliente);
        }

        for (String nome : nomesClientes) {
            JLabel clienteLabel = new JLabel(nome);
            clienteLabel.setForeground(Color.BLACK);
            clienteLabel.setFont(new Font("Arial", Font.PLAIN, 14));
            clienteLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
            clienteLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent evt) {
                    abrirJanelaEdicao(nome);
                }
            });
            resultPanel.add(clienteLabel);
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void pesquisarNomeCliente() {
        String nomePesquisa = searchField.getText().trim().toLowerCase();
        if (nomePesquisa.isEmpty()) {
            mostrarResultado("Digite um nome para pesquisar.");
            return;
        }

        resultPanel.removeAll();

        for (Venda venda : vendas) {
            if (venda.getCliente().getNome().toLowerCase().contains(nomePesquisa)) {
                JLabel clienteLabel = new JLabel(venda.getCliente().getNome());
                clienteLabel.setForeground(Color.BLACK);
                clienteLabel.setFont(new Font("Arial", Font.PLAIN, 14));
                clienteLabel.setBorder(new EmptyBorder(5, 5, 5, 5));
                clienteLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent evt) {
                        abrirJanelaEdicao(venda.getCliente().getNome());
                    }
                });
                resultPanel.add(clienteLabel);
            }
        }

        resultPanel.revalidate();
        resultPanel.repaint();
    }

    private void mostrarResultado(String mensagem) {
        outputArea.setText(mensagem);
    }

    private void limparCampos() {
        cpfField.setText("");
        nomeField.setText("");
        produtoField.setText("");
        precoField.setText("");
        modeloField.setText("");
        tamanhoField.setText("");
    }

    private void abrirJanelaEdicao(String nomeCliente) {
        Cliente cliente = null;
        for (Venda venda : vendas) {
            if (venda.getCliente().getNome().equals(nomeCliente)) {
                cliente = venda.getCliente();
                break;
            }
        }
        if (cliente != null) {
            new JanelaEdicao(cliente, vendas).setVisible(true);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new App().setVisible(true);
            }
        });
    }
}