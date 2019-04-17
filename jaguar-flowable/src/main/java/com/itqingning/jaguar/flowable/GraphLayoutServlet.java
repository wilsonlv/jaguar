package com.itqingning.jaguar.flowable;

import com.yworks.yfiles.server.graphml.folding.FoldedLayoutGraph;
import com.yworks.yfiles.server.graphml.support.GraphRoundtripSupport;
import y.layout.LayoutOrientation;
import y.layout.Layouter;
import y.layout.hierarchic.IncrementalHierarchicLayouter;
import y.layout.orthogonal.OrthogonalGroupLayouter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by lvws on 2019/4/16.
 */
@WebServlet(urlPatterns = "/graph_layout")
public class GraphLayoutServlet extends HttpServlet {

    /**
     * 流程布局
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        GraphRoundtripSupport support = new GraphRoundtripSupport();
        FoldedLayoutGraph graph = (FoldedLayoutGraph) support.createRoundtripGraph();
        support.readGraph(request, graph);

        String type = request.getParameter("type");
        String minimumLayerDistance = request.getParameter("minimumLayerDistance");
        String nodeToEdgeDistance = request.getParameter("nodeToEdgeDistance");
        String gridGap = request.getParameter("gridGap");

        Layouter layouter;
        if ("grid".equals(type)) {
            OrthogonalGroupLayouter orthogonalGroupLayouter = new OrthogonalGroupLayouter();
            orthogonalGroupLayouter.setLayoutOrientation(LayoutOrientation.RIGHT_TO_LEFT);
            orthogonalGroupLayouter.setConsiderNodeLabelsEnabled(true);
            orthogonalGroupLayouter.setGrid(Integer.parseInt(gridGap));
            layouter = orthogonalGroupLayouter;
        } else {
            IncrementalHierarchicLayouter ihl = new IncrementalHierarchicLayouter();
            ihl.setLayoutOrientation(LayoutOrientation.LEFT_TO_RIGHT);//控制布局方向
            ihl.setConsiderNodeLabelsEnabled(true);

            if ("hierarchicOrth".equals(type)) {
                ihl.setOrthogonallyRouted(true);
            } else {
                ihl.setOrthogonallyRouted(false);
            }

            ihl.setMinimumLayerDistance(Double.parseDouble(minimumLayerDistance));//没层之间的最小距离
            ihl.setNodeToEdgeDistance(Double.parseDouble(nodeToEdgeDistance));
            layouter = ihl;
        }
        graph.doLayout(layouter);

        support.sendGraph(graph, response);

    }

}
