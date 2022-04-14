/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsf.managedbean;

import ejb.session.stateless.MessageOfTheDayEntitySessionBeanLocal;
import ejb.session.stateless.OrderEntitySessionBeanLocal;
import entity.MessageOfTheDayEntity;
import entity.OrderEntity;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.event.ActionEvent;
import javax.inject.Named;
import javax.inject.Inject;
import javax.enterprise.context.RequestScoped;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.axes.cartesian.CartesianScaleLabel;
import org.primefaces.model.charts.axes.cartesian.CartesianScales;
import org.primefaces.model.charts.axes.cartesian.linear.CartesianLinearAxes;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

/**
 *
 * @author brennanlee
 */
@Named(value = "indexManagedBean")
@RequestScoped
public class IndexManagedBean implements Serializable {

    @EJB(name = "MessageOfTheDayEntitySessionBeanLocal")
    private MessageOfTheDayEntitySessionBeanLocal messageOfTheDayEntitySessionBeanLocal;

    @EJB(name = "OrderEntitySessionBeanLocal")
    private OrderEntitySessionBeanLocal orderEntitySessionBeanLocal;
    
    

    private String salesLineModelHex1;
    private LineChartModel salesLineModel;
    private List<List<OrderEntity>> orderEntitiesList;
    private List<MessageOfTheDayEntity> listOfMessageOfTheDay;
    private List<MessageOfTheDayEntity> recentThreeMotd;
    
    public IndexManagedBean() {
        salesLineModelHex1 = "#2AECFF";
    }

    @PostConstruct
    public void postConstruct() {
        orderEntitiesList = orderEntitySessionBeanLocal.retrieveAllOrdersInPastYear();
        System.out.println(orderEntitiesList.size());
        for (List<OrderEntity> listOfOrders : orderEntitiesList) {
            System.out.println(listOfOrders.size());
        }
        createSalesLineChartModel();
        
    }

    public void createSalesLineChartModel() {
        salesLineModel = new LineChartModel();
        ChartData data = new ChartData();

        LineChartDataSet dataSet = new LineChartDataSet();
        List<Object> values = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        if (!getOrderEntitiesList().isEmpty()) {
            for (List<OrderEntity> orderEntities : getOrderEntitiesList()) {
                if (!orderEntities.isEmpty()) {
                    labels.add(orderEntities.get(0).getTransactionDateTime().getMonth() + " " + orderEntities.get(0).getTransactionDateTime().getYear());

                    BigDecimal totalSales = BigDecimal.ZERO;

                    for (OrderEntity orderEntity : orderEntities) {
                        totalSales = totalSales.add(orderEntity.getTotalAmount());
                    }

                    values.add(totalSales); 
                }

            }

            dataSet.setData(values);
            dataSet.setFill(false);
            dataSet.setLabel("");
            dataSet.setBorderColor(salesLineModelHex1);
            dataSet.setLineTension(0.1);
            data.addChartDataSet(dataSet);

            data.setLabels(labels);

            //Options
            LineChartOptions options = new LineChartOptions();

            Title title = new Title();
            title.setDisplay(true);
            title.setText("Monthly Total Sales Report Chart");
            options.setTitle(title);

            CartesianScales cScales = new CartesianScales();

            CartesianLinearAxes xLinearAxes = new CartesianLinearAxes();
            xLinearAxes.setStacked(true);
            xLinearAxes.setOffset(true);

            CartesianLinearAxes yLinearAxes = new CartesianLinearAxes();
            yLinearAxes.setStacked(true);
            yLinearAxes.setOffset(true);

            CartesianScaleLabel xScaleLabel = new CartesianScaleLabel();
            xScaleLabel.setDisplay(true);
            xScaleLabel.setLabelString("Month Year");

            CartesianScaleLabel yScaleLabel = new CartesianScaleLabel();
            yScaleLabel.setDisplay(true);
            yScaleLabel.setLabelString("Amount ($)");

            xLinearAxes.setScaleLabel(xScaleLabel);
            yLinearAxes.setScaleLabel(yScaleLabel);

            cScales.addXAxesData(xLinearAxes);
            cScales.addYAxesData(yLinearAxes);

            options.setScales(cScales);

            salesLineModel.setOptions(options);
            salesLineModel.setData(data);
        }

    }

    //getters and setters
    public String getSalesLineModelHex1() {
        return salesLineModelHex1;
    }

    public void setSalesLineModelHex1(String salesLineModelHex1) {
        this.salesLineModelHex1 = salesLineModelHex1;
    }

    public LineChartModel getSalesLineModel() {
        return salesLineModel;
    }

    public void setSalesLineModel(LineChartModel salesLineModel) {
        this.salesLineModel = salesLineModel;
    }

    public List<List<OrderEntity>> getOrderEntitiesList() {
        return orderEntitiesList;
    }

    public void setOrderEntitiesList(List<List<OrderEntity>> orderEntitiesList) {
        this.orderEntitiesList = orderEntitiesList;
    }

    public List<MessageOfTheDayEntity> getListOfMessageOfTheDay() {
        return listOfMessageOfTheDay;
    }

    public void setListOfMessageOfTheDay(List<MessageOfTheDayEntity> listOfMessageOfTheDay) {
        this.listOfMessageOfTheDay = listOfMessageOfTheDay;
    }

    public List<MessageOfTheDayEntity> getRecentThreeMotd() {
        return recentThreeMotd;
    }

    public void setRecentThreeMotd(List<MessageOfTheDayEntity> recentThreeMotd) {
        this.recentThreeMotd = recentThreeMotd;
    }

}
