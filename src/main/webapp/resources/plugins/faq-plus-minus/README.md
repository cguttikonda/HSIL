# jQuery Accordion
[![jQuery Accordion on NPM](https://img.shields.io/npm/v/@gardelin/jquery-accordion.svg?style=flat-square)](https://www.npmjs.com/package/@gardelin/jquery-accordion)
[![Standard JavaScript Style](https://img.shields.io/badge/code_style-standard-brightgreen.svg?style=flat-square)](http://standardjs.com/)

Simple jQuery accordion library, perfect for FAQ (Frequently Asked Question) list.
![Accordion](accordion.png)

## Usage

    <div  class="faq">
        <div  class="faqitem">
            <div  class="header">Test</div>
            <div  class="content">test content</div>
        </div>
        <div  class="faqitem">
            <div  class="header">Test</div>
            <div  class="content">test content</div>
        </div>
        <div  class="faqitem">
        <div  class="header">Test</div>
            <div  class="content">test content</div>
        </div>
    </div>

    <script>
        $(".faq").accordion(options);
    </script>

## Options

| Option  | Type | Default value | Description |
| ----- | ----- | ----- | ----- |
| duration | number | 200 | Animation duration in ms |
| closeOthers | boolean | true | Show only one element at the same time |
| questionClass | string | '.question' | Question class selector |
| answerClass | string | '.answer' | Answer class selector |
| itemClass | string | '.faqitem' | Accordion item class selector |

## Events
| Name  | Description |
| ----- | ----- |
| jqueryaccordioninit | Accordion is initialized |
| jqueryaccordiontoggle | Before accordion item is opened/closed |
| jqueryaccordiontoggled | After accordion item is opened/closed |
| jqueryaccordiondestory | Accordion is destroyed |
