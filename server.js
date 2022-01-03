const express = require('express')
const app = express()
const port = 3000
const API_KEY = "Y7M0OEMFWKC0M37L"
const request = require('request')


app.get('/', (req, res) => {
    res.send(`Welcome to Eitan\'s server!`)
})

app.use(function (req, res, next) {
    res.header('Access-Control-Allow-Origin', '*')
    res.header('Access-Control-Allow-Methods', 'GET,PUT,POST,DELETE,OPTIONS')
    res.header('Access-Control-Allow-Headers', 'Content-Type, Authorization, Content-Length, X-Requested-With')
    next()
})

app.get('/:companyName', (req, res) => {
    const companyName = req.params.companyName
    const url = `https://www.alphavantage.co/query?function=GLOBAL_QUOTE&symbol=${companyName}&apikey=${API_KEY}`
    let found = false
    let price = 0
    let symbol = ""
    
    request.get({
        url: url,
        json: true,
        headers: {
            'User-Agent': 'request'
        }
    },
    (err, result, data) => {
        if (err) {
            console.log('Error:', err);
        } else if (result.statusCode !== 200) {
            console.log('Status:', result.statusCode);
        } else if ( !Object.keys(data["Global Quote"]).length) {
            found = false
        } else {        // bruteforce rapid calls failure prevention
            found = true
            symbol = data["Global Quote"]['01. symbol']
            price = data["Global Quote"]['05. price']
            console.log(`Request was made for ticket: ${companyName}\nApi Response:`,data)
        }
        res.send({
            found,
            price,
            symbol
        })
    })

})


app.listen(port, () => {
    console.log(`server up and running on port ${port}`)
})