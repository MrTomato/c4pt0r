const https = require('https');
const cheerio = require('cheerio');

const options = {
    hostname: 'www.womentalkbusiness.info',
    path: '/captiveportal/tmobile/HotSpot.html',
    headers: {'User-Agent': 'Mozilla/5.0'}
};


request = https.get(options);

function doParsing(body) {
    const $ = cheerio.load(body);
    var result = $('input[name=password]');
    console.log(result.data());

}

request.on('response', function (response) {
    response.setEncoding('utf8');

    var body = "";
    response.on('data', function (chunk) {
        body = body + chunk;
    });

    response.on('end', function () {
        doParsing(body);

    });
});
request.end();
