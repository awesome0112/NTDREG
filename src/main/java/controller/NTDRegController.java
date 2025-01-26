package controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import utils.CIAutil.model.ast.CIANode;
import utils.FilePath;
import utils.autoUnitTestUtil.testGeneration.NTDTestGeneration.NTDTesting;
import utils.autoUnitTestUtil.testGeneration.TestGeneration;
import utils.autoUnitTestUtil.testResult.ParameterData;
import utils.autoUnitTestUtil.testResult.TestData;
import utils.autoUnitTestUtil.testResult.TestResult;
import utils.cloneProjectUtil.CloneProjectUtil;
import utils.cloneProjectUtil.projectTreeObjects.Folder;
import utils.cloneProjectUtil.projectTreeObjects.JavaFile;
import utils.cloneProjectUtil.projectTreeObjects.ProjectTreeObject;
import utils.cloneProjectUtil.projectTreeObjects.Unit;
import utils.uploadUtil.ConcolicUploadUtil;
import utils.uploadUtil.NTDUploadUtil;
import utils.versionCompareUtil.model.VersionCompareResponse;
import utils.versionCompareUtil.model.ast.node.Node;
import utils.versionCompareUtil.utils.Utils;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class NTDRegController implements Initializable {

    private FileChooser fileChooser = new FileChooser();
    private File V1ChoseFile;
    private File V2ChoseFile;
    private Unit choseUnit;
    private TestGeneration.Coverage choseCoverage;

    @FXML
    private Label V1FilePreview;

    @FXML
    private Label V2FilePreview;

    @FXML
    private Label alertLabel;

    @FXML
    private Label allTestCasesCoverageLabel;

    @FXML
    private ChoiceBox<String> coverageChoiceBox;

    @FXML
    private Label executeTimeLabel;

    @FXML
    private Button generateButton;

    @FXML
    private ListView<ParameterData> generatedTestDataListView;

    @FXML
    private Label outputLabel;

    @FXML
    private TreeView<ProjectTreeObject> projectTreeView;

    @FXML
    private Label requireCoverageLabel;

    @FXML
    private Label sourceCodeCoverageLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private VBox testCaseDetailVBox;

    @FXML
    private Label testCaseIDLabel;

    @FXML
    private ListView<TestData> testCaseListView;

    @FXML
    private Label testingTimeLabel;

    @FXML
    private Button uploadFilesButton;

    @FXML
    private Label usedMemoryLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        uploadFilesButton.setDisable(true);
        generateButton.setDisable(true);
        coverageChoiceBox.setDisable(true);
        coverageChoiceBox.getItems().addAll("Statement coverage", "Branch coverage");
        coverageChoiceBox.setOnAction(this::selectCoverage);
        testCaseDetailVBox.setDisable(true);
        allTestCasesCoverageLabel.setDisable(true);
        testingTimeLabel.setDisable(true);
        usedMemoryLabel.setDisable(true);

        testCaseListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TestData>() {
            @Override
            public void changed(ObservableValue<? extends TestData> observableValue, TestData concolicTestData, TestData t1) {
                TestData testData = testCaseListView.getSelectionModel().getSelectedItem();
                if (testData != null) {
                    setTestCaseDetail(testData);
                    testCaseDetailVBox.setDisable(false);
                }
            }
        });
    }

    private void setTestCaseDetail(TestData testData) {
        testCaseIDLabel.setText("   Test case ID: " + testData.getTestCaseID());
        sourceCodeCoverageLabel.setText("   Source code coverage: " + testData.getSourceCodeCoverage());
        requireCoverageLabel.setText("   Required coverage: " + testData.getRequiredCoverage());
        executeTimeLabel.setText("   Execute time: " + testData.getExecuteTime());
        outputLabel.setText("   Output: " + testData.getOutput());
        statusLabel.setText("   Status: " + testData.getStatus());

        generatedTestDataListView.getItems().clear();
        generatedTestDataListView.getItems().addAll(testData.getParameterDataList());
    }

    private void selectCoverage(ActionEvent actionEvent) {
        generateButton.setDisable(false);

        String coverage = coverageChoiceBox.getValue();
        if (coverage.equals("Statement coverage")) {
            choseCoverage = TestGeneration.Coverage.STATEMENT;
        } else if (coverage.equals("Branch coverage")) {
            choseCoverage = TestGeneration.Coverage.BRANCH;
        } else if (coverage.equals("")) {
            // do nothing!
        } else {
            throw new RuntimeException("Invalid coverage");
        }
    }

    @FXML
    void generateButtonClicked(MouseEvent event) {
        resetTestCaseDetailVBox();
        resetGeneratedTestCasesInfo();
        alertLabel.setText("");

        TestResult result;
        try {
            result = NTDTesting.runFullNTD(choseUnit.getPath(), choseUnit.getMethodName(), choseUnit.getClassName(), choseCoverage);
        } catch (Exception e) {
            e.printStackTrace();
            alertLabel.setTextFill(Paint.valueOf("red"));
            alertLabel.setText("Examined unit contains cases we haven't handle yet!");
            return;
        }

        allTestCasesCoverageLabel.setText("   All test cases coverage: " + result.getFullCoverage() + "%");
        allTestCasesCoverageLabel.setDisable(false);

        testingTimeLabel.setText("   Testing time: " + result.getTestingTime() + "ms");
        testingTimeLabel.setDisable(false);

        usedMemoryLabel.setText("   Used memory: " + result.getUsedMemory() + "MB");
        usedMemoryLabel.setDisable(false);


        testCaseListView.getItems().addAll(result.getFullTestData());
    }

    @FXML
    void selectUnit(MouseEvent event) {
        TreeItem<ProjectTreeObject> selectedItem = projectTreeView.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            ProjectTreeObject treeObject = selectedItem.getValue();
            if (treeObject instanceof Unit) {
                choseUnit = (Unit) treeObject;
                coverageChoiceBox.setDisable(false);
                coverageChoiceBox.setValue("");
                generateButton.setDisable(true);
            } else {
                choseUnit = null;
                coverageChoiceBox.setDisable(true);
                coverageChoiceBox.setValue("");
                generateButton.setDisable(true);
            }
        }
    }

    @FXML
    void selectV1ButtonClicked(MouseEvent event) {
        V1FilePreview.setText("Please compress project to .zip file before uploading");
        uploadFilesButton.setDisable(true);
        V1ChoseFile = fileChooser.showOpenDialog(new Stage());
        if (V1ChoseFile != null) {
            V1FilePreview.setText(V1ChoseFile.getAbsolutePath());
            if (V2ChoseFile != null) {
                uploadFilesButton.setDisable(false);
            }
        }
    }

    @FXML
    void selectV2ButtonClicked(MouseEvent event) {
        V2FilePreview.setText("Please compress project to .zip file before uploading");
        uploadFilesButton.setDisable(true);
        V2ChoseFile = fileChooser.showOpenDialog(new Stage());
        if (V2ChoseFile != null) {
            V2FilePreview.setText(V2ChoseFile.getAbsolutePath());
            if (V1ChoseFile != null) {
                uploadFilesButton.setDisable(false);
            }
        }
    }

    @FXML
    void uploadFilesButtonClicked(MouseEvent event) {
        reset();
        try {
            long startTime = System.nanoTime();

            CloneProjectUtil.deleteFilesInDirectory(FilePath.uploadedProjectPath);
            ConcolicUploadUtil.javaUnzipFile(V2ChoseFile.getPath(), FilePath.uploadedProjectPath);

            Path rootPackage = CloneProjectUtil.findRootPackage(Paths.get(FilePath.uploadedProjectPath));
            if (rootPackage == null) throw new RuntimeException("Invalid project");


            VersionCompareResponse compareResult = Utils.getCompare(V1ChoseFile, V2ChoseFile);

            Folder folder = CloneProjectUtil.cloneProject(rootPackage.toString(), FilePath.clonedProjectPath);


            long endTime = System.nanoTime();
            double duration = (endTime - startTime) / 1000000.0;
            duration = (double) Math.round(duration * 100) / 100;

            projectTreeView.setCellFactory(tv -> new TextFieldTreeCell<>() {
                @Override
                public void updateItem(ProjectTreeObject item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        setText(item.getName());
                        setTextFill(Color.web(item.getColor()));
                    }
                }
            });

            TreeItem<ProjectTreeObject> rootFolder = switchToTreeItem(folder, compareResult);

            projectTreeView.setRoot(rootFolder);
            projectTreeView.refresh();
            alertLabel.setTextFill(Paint.valueOf("green"));
            alertLabel.setText("Upload time " + duration + "ms");
        } catch (Exception e) {
            e.printStackTrace();
            alertLabel.setTextFill(Paint.valueOf("red"));
            alertLabel.setText("INVALID PROJECT ZIP FILE (eg: not a zip file, project's source code contains cases we haven't handled, ...)");
        }
    }

    private TreeItem<ProjectTreeObject> switchToTreeItem(ProjectTreeObject treeObject, VersionCompareResponse compareResult) {
        if (treeObject instanceof Folder) {
            TreeItem<ProjectTreeObject> item = new TreeItem<>(treeObject, new ImageView(new Image("\\img\\folder_icon.png")));
            List<ProjectTreeObject> children = ((Folder) treeObject).getChildren();
            for (ProjectTreeObject child : children) {
                item.getChildren().add(switchToTreeItem(child, compareResult));
            }
            return item;
        } else if (treeObject instanceof JavaFile) {
            TreeItem<ProjectTreeObject> item = new TreeItem<>(treeObject, new ImageView(new Image("\\img\\java_file_icon.png")));
            List<Unit> units = ((JavaFile) treeObject).getUnits();
            for (Unit unit : units) {
                item.getChildren().add(switchToTreeItem(unit, compareResult));
            }
            return item;
        } else if (treeObject instanceof Unit) {
            TreeItem unitTreeItem = new TreeItem<>(treeObject, new ImageView(new Image("\\img\\unit_icon.png")));
            Unit unitTreeItemValue = (Unit) unitTreeItem.getValue();
            setChangedAndImpactedUnitColor(unitTreeItemValue, compareResult);
            return unitTreeItem;
        } else {
            throw new RuntimeException("Invalid ProjectTreeObject");
        }
    }

    private void setChangedAndImpactedUnitColor(Unit unit, VersionCompareResponse compareResult) {
        List<Node> changedNodes = compareResult.getChangedNodes();
        List<Node> addedNodes = compareResult.getAddedNodes();
        List<CIANode> impactedNodes = compareResult.getImpactedNodes();

        for (Node node : changedNodes) {
            if (unit.getMethodName().equals(node.getSimpleName())) {
                unit.setColor("blue");
                break;
            }
        }

        for (Node node : addedNodes) {
            if (unit.getMethodName().equals(node.getSimpleName())) {
                unit.setColor("red");
                break;
            }
        }

        for (CIANode node : impactedNodes) {
            if (unit.getMethodName().equals(node.getMethodSimpleName())) {
                unit.setColor("yellow");
                break;
            }
        }
    }

    private void reset() {
        projectTreeView.setRoot(null);
        coverageChoiceBox.setValue("");
        coverageChoiceBox.setDisable(true);
        generateButton.setDisable(true);
        alertLabel.setText("");
        resetGeneratedTestCasesInfo();
        resetTestCaseDetailVBox();
    }

    private void resetGeneratedTestCasesInfo() {
        testCaseListView.getItems().clear();
        allTestCasesCoverageLabel.setText("   All test cases coverage:");
        allTestCasesCoverageLabel.setDisable(true);
        testingTimeLabel.setText("   Testing time:");
        testingTimeLabel.setDisable(true);
        usedMemoryLabel.setText("   Used memory:");
        usedMemoryLabel.setDisable(true);
    }

    private void resetTestCaseDetailVBox() {
        testCaseDetailVBox.setDisable(true);
        testCaseIDLabel.setText("   Test case ID:");
        sourceCodeCoverageLabel.setText("   Source code coverage:");
        requireCoverageLabel.setText("   Required coverage:");
        executeTimeLabel.setText("   Execute time:");
        outputLabel.setText("   Output:");
        statusLabel.setText("   Status:");
        generatedTestDataListView.getItems().clear();
    }

}
