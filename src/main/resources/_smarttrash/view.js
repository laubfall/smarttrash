///<reference path="view_types.ts"/>
///<reference path="../d3js/d3.d.ts"/>
///<reference path="../datejs/datejs.d.ts"/>
///<reference path="../ckeditor/ckeditor.d.ts"/>
function computeAndRenderView(containerDS, dataService, eventHandler) {
    var view = new View(containerDS);
    // --> START Dummy-Code faking some Data (Notes and Flows)
    // create the dummy data
    var flowsOfCurrentHierarchy = [];
    var notes = [];
    var es = dataService;
    flowsOfCurrentHierarchy = es.findFlows();
    notes = es.findNotes(containerDS.startDate, containerDS.endDate);
    var ds = {
        flowsForOneHierarchy: flowsOfCurrentHierarchy,
        notes: notes
    };
    // create the flow surfaces
    var flowModel = view.createFlowSurfaces(ds);
    // create the container (x- and y-Dimensions)
    var containerModel = view.createContainer();
    renderView(containerDS, flowModel, eventHandler);
}
/**
 * Created by Daniel on 25.08.15.
 */
function renderView(containerDS, flowModel, eventHandler) {
    // render graph axis
    var x = d3.time.scale().domain([containerDS.startDate, containerDS.endDate]).range([0, containerDS.width - 82]);
    var xAxis = d3.svg.axis().scale(x).ticks(d3.time.days).tickSize(200, 0).orient("bottom");
    d3.select("svg").append("g").attr("class", "axis").attr("height", containerDS.height).call(xAxis);
    // create the surface elements for every flow. This surfaces are invisible but offers the possibility
    // to select a flow.
    var flowSurfaceData = [];
    for (var fIdx = 0; fIdx < flowModel.length; fIdx++) {
        var low = flowModel[fIdx];
        var allPoints;
        if (fIdx == 0) {
            // we use the x-axis as the lower clipping path
            var xAxisPoints = [];
            var lmPoints = low.getBorders().second().getPoints();
            for (var lmP in lmPoints) {
                var p = lmPoints[lmP];
                p = new Pair(p.first(), p.second());
                p.setSecond(0);
                xAxisPoints.push(p);
            }
            allPoints = lmPoints.concat(xAxisPoints.reverse());
        }
        else {
            var high = flowModel[fIdx - 1];
            var lowPoints = low.getBorders().second().getPoints();
            var highPoints = high.getBorders().second().getPoints().reverse();
            allPoints = lowPoints.concat(highPoints);
        }
        flowSurfaceData.push(new Pair(low, allPoints));
    }
    d3.select("svg").append("g").attr("class", "flowClipPath").selectAll("path").data(flowSurfaceData).enter().append("path").attr("d", function (data, index) {
        var attrD = pointArrayToSvgPath(data.second(), index);
        attrD = attrD.concat(" Z");
        return attrD;
    }).on("click", function (data, index) {
        eventHandler.flowClicked(data.first().getFlow());
    });
    ;
    for (var flowMIdx in flowModel) {
        var fm = flowModel[flowMIdx];
        // render the top-line of every flow surface
        d3.select("svg").append("g").classed("flow", true).selectAll("path").data([fm.getBorders().second()]).enter().append("path").attr("d", lineModelToSvgPath).style({ "stroke-width:": "8", "stroke": "rgb(255,0,10)", "fill": "none" });
        for (var subFlowIdx in fm.getNextInHierarchyFlows()) {
            var lm = fm.getNextInHierarchyFlows()[subFlowIdx];
            d3.select("svg").append("g").classed("subflow", true).selectAll("path").data([lm]).enter().append("path").attr("d", lineModelToSvgPath).style({ "stroke-width:": "8", "stroke": "rgb(50,50,50)", "fill": "none" });
        }
        // render the note bumps for the flow surfaces.
        d3.select("svg").append("g").classed("notebump", true).selectAll("rect").data(fm.getNoteBumps()).enter().append("rect").attr("x", function (d, idx) {
            return d.getUpperLeft().first();
        }).attr("y", function (d, idx) {
            return d.getUpperLeft().second();
        }).attr("width", function (d, idx) {
            return d.getDimenstion().first();
        }).attr("height", function (d, idx) {
            return d.getDimenstion().second();
        }).on("mouseover", function (data, index) {
            eventHandler.noteBumpClicked(data);
        }).style({ "fill": "blue" });
    }
    var t = "translate(40, 250)";
    d3.select("svg").selectAll("g.axis").attr("transform", "translate(40, 60)");
    d3.select("svg").selectAll("g.flow, g.subflow, g.notebump, g.flowClipPath").attr("transform", t + ",scale(1,-1)");
}
function forward(containerDS, es, eventHandler) {
    containerDS.startDate.addDays(1);
    containerDS.endDate.addDays(1);
    clearView();
    computeAndRenderView(containerDS, es, eventHandler);
}
function backward(containerDS, es, eventHandler) {
    containerDS.startDate.addDays(-1);
    containerDS.endDate.addDays(-1);
    clearView();
    computeAndRenderView(containerDS, es, eventHandler);
}
function today(containerDS, es, eventHandler) {
    var today = new Date();
}
function clearView() {
    d3.select("svg").selectAll("g").remove();
}
function lineModelToSvgPath(lm, idx) {
    return pointArrayToSvgPath(lm.getPoints(), idx);
}
function pointArrayToSvgPath(points, idx) {
    var d = "";
    for (var i = 0; i < points.length; i++) {
        var p = points[i];
        if (i == 0) {
            d = d.concat("M");
        }
        else {
            d = d.concat("L");
        }
        d = d.concat(p.first().toString()).concat(",").concat(p.second().toString());
        if (i < points.length - 1) {
            d = d.concat(" ");
        }
    }
    return d;
}
