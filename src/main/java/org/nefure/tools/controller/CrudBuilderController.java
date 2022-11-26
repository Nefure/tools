package org.nefure.tools.controller;

import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableListBase;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import org.nefure.fxscaffold.annotion.Component;
import org.nefure.fxscaffold.annotion.Resource;
import org.nefure.tools.dao.TableDao;
import org.nefure.tools.entity.Table;
import org.nefure.tools.service.CrudBuilderService;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nefure
 * @since 2022/11/25 0:04
 */
@Component
public class CrudBuilderController {


    public TextField urlInput;
    public TextField userNameInput;
    public PasswordField passwordInput;
    public ComboBox<Table> tableSelect;
    public TextField simpleClassNameInput;
    public TextField targetDicInput;
    public TextField prefixInput;

    private static final String DRIVER = "com.mysql.jdbc.Driver";
    public VBox fieldBox;

    private String url;
    private String userName;
    private String passwd;

    private boolean isInit;

    private final List<Table> tables = new ArrayList<>();
    private Table table;

    private ChangeListener<Boolean> listener;

    @Resource
    private final TableDao tableDao = new TableDao();

    @Resource
    private CrudBuilderService crudBuilderService;

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println("Sorry,can`t find the Driver!");
            throw new RuntimeException(e);
        }
    }

    private void flashFormData() {
        url = urlInput.getText();
        userName = userNameInput.getText();
        passwd = passwordInput.getText();
    }


    public void connect() {

        flashFormData();

        try {

            tables.clear();
            tables.addAll(tableDao.selectAll(url, userName, passwd));

            if (!isInit) {
                initTableSelector();
                ((CheckBox) ((FlowPane) fieldBox.getChildren().get(0)).getChildren().get(3)).selectedProperty()
                        .addListener((observable, oldValue, newValue) -> table.getColumns().get(0).setIgnored(newValue));
            }
            tableSelect.setValue(tables.get(0));
        } catch (Exception e) {
            //数据库连接失败异常处理
            e.printStackTrace();
        }
    }

    private void initTableSelector() {
        tableSelect.setItems(new ObservableListBase<>() {
            @Override
            public Table get(int index) {
                return tables.get(index);
            }

            @Override
            public int size() {
                return tables.size();
            }

        });
        tableSelect.getSelectionModel().selectedIndexProperty().addListener((observable, oldValue, newValue) -> {
            table = tables.get(newValue.intValue());
            tableDao.selectAllColumns(url, userName, passwd, table);
            loadTableDetails();
        });
        isInit = true;
    }

    private void loadTableDetails() {
        ObservableList<Node> children = fieldBox.getChildren();
        List<Table.Column> columns = table.getColumns();
        for (Node child : children) {
            child.setVisible(false);
        }
        FlowPane row;
        for (int i = 0; i < columns.size(); i++) {
            Table.Column column = columns.get(i);
            if (i >= children.size()) {
                children.add(buildFieldRow(children));
            }
            reuseFieldRow(row = (FlowPane) children.get(i), column);
            row.setVisible(true);
        }
        fieldBox.setVisible(false);
        fieldBox.setVisible(true);
    }

    private void reuseFieldRow(FlowPane node, Table.Column column) {
        ObservableList<Node> children = node.getChildren();
        ((Label) children.get(0)).setText(column.getName());
        ((Label) children.get(1)).setText(column.getTypeName());
        ((Label) children.get(2)).setText(column.getClassName());
        ((CheckBox) children.get(3)).setSelected(column.getIgnored());
    }

    /**
     * 根据模板创建新的视图行
     *
     * @return 视图
     */
    private FlowPane buildFieldRow(ObservableList<Node> rows) {
        FlowPane temp = (FlowPane) rows.get(0);

        FlowPane flowPane = new FlowPane();
        flowPane.setPrefWidth(temp.getPrefWidth());
        flowPane.setPrefHeight(temp.getPrefHeight());

        ObservableList<Node> tempNodes = temp.getChildren();
        ObservableList<Node> children = flowPane.getChildren();

        Label label = (Label) tempNodes.get(0);
        for (int i = 0; i < 3; i++) {
            Label tmp = new Label();
            tmp.setPrefWidth(label.getPrefWidth());
            tmp.setPrefHeight(label.getPrefHeight());
            tmp.setAlignment(label.getAlignment());
            children.add(tmp);
        }

        CheckBox checkBox = (CheckBox) tempNodes.get(3);
        CheckBox newCheckBox = new CheckBox();
        newCheckBox.setText(checkBox.getText());
        newCheckBox.setPrefWidth(checkBox.getPrefWidth());
        newCheckBox.setPrefHeight(checkBox.getPrefHeight());
        int idx = rows.size();
        newCheckBox.selectedProperty()
                .addListener((observable, oldValue, newValue) -> table.getColumns().get(idx).setIgnored(newValue));
        children.add(newCheckBox);

        flowPane.getStyleClass().add("item");

        return flowPane;
    }

    public void build() throws Exception {
        crudBuilderService.build(table, prefixInput.getText(), simpleClassNameInput.getText(), targetDicInput.getText());
    }
}
