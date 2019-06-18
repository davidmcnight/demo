<?php
/**
 * Created by PhpStorm.
 * User: dmcnight
 * Date: 2/23/18
 * Time: 1:29 PM
 */
use PatriotIssue\Domain\Factory\Factory;

$app->get(
    '/api/v1/patriot_issue/models/:className',
    function ($className) use ($app) {
        $service = Factory::createService($className);
        $results = $service->getAll();
        printNicely($results);
    });

$app->get(
    '/api/v1/patriot_issue/models/find/:className',
    function ($className) use ($app) {
        $parameters = $app->request->params();
        $service = Factory::createService($className);
        $results = $service->find($parameters);
        echo json_encode($results);
    });

?>