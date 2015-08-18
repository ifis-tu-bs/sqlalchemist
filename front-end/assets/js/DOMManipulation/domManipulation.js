/**
 * defines a function that adds an event to an node
 * @param : node : the node
 *          type : the type of event
 *          fn   : the function that should be fire
 */
function myAddEvent(node, type, fn) {
    if (node.addEventListener) {
        node.addEventListener (type, fn, false);
    }
    else if (node.attachEvent) {
        node.attachEvent ('on'+type, fn);
    }
}

/**
 * defines a function that removes an event from an node
 * @param : node : the node
 *          type : the type of event
 *          fn   : the function that should be remove
 */
function myRemoveEvent(node, type, fn) {
    if (node.removeEventListener) {
        node.removeEventListener (type, fn, false);
    }
    else if (node.detachEvent) {
        node.detachEvent ('on'+type, fn);
    }
};

/**
 *
 */
function getTextFromNode(node) {
    var i, result, text, child;
    result = '';
    for (i = 0; i < node.childNodes.length; i++) {
        child = node.childNodes[i];
        text = null;
        if (child.nodeType === 1) {
            text = getTextFromNode(child);
        } 
        else if (child.nodeType === 3) {
            text = child.nodeValue;
        }
        if (text) {
            result += text;
        }
    }
    return result;
}

/**
 * defines an internal function which scales the objects of the classes 'game.TextInputElement',
 * 'game.TextOutputElement' and 'game.ClickableElement'
 * @author : Christian Reineke
 * @param : elem : a object
 */
game.scaleElement = function(object) {
    
    var canvasWidth      = me.video.getPos().width;
    var canvasHeight     = me.video.getPos().height;
    var canvasOffsetLeft = me.video.getPos().left;
    var canvasOffsetTop  = me.video.getPos().top;
    
    // get values
    var width      = canvasWidth * object.relWidth / 100;
    var height     = canvasHeight * object.relHeight / 100;
    var leftMargin = canvasWidth * object.relLeft / 100;
    var topMargin  = canvasHeight * object.relTop / 100;
    
    if (object.row == 1) {
    	var lineHeight = height * 0.9;
    }
    else if (object.row == 0) {
    	console.log("ERROR: Division by 0 in game.scaleElement");
    	var lineHeight = height * 0.9;
    }
    else {
        var lineHeight = height / object.rows;
    }
    
    var fontSize   = lineHeight;

    // set values
    object.elem.style.left   = (leftMargin + canvasOffsetLeft) + "px";
    object.elem.style.top    = (topMargin + canvasOffsetTop) + "px";
    object.elem.style.width  = width + 'px';
    object.elem.style.height = height + 'px';
    
    object.getNode().style.lineHeight = lineHeight + 'px';
    object.getNode().style.fontSize   = fontSize + 'px';
   
}

/**
 * defines a scaleable object for text input
 * @author : Christian Reineke
 */
game.TextInputElement = me.Renderable.extend ({

    /**
     * constructor
     * @param : tag       : 'input' or 'textarea'
     *          type      : the type of a 'input' element or the name of a 'textarea' element as string
     *          wrapperId : a unique alphanumeric string
     *          fieldId   : a unique alphanumeric string
     *          width     : the width of the element in percent of the width of the canvas
     *          height    : the height of the element in percent of the height of the canvas
     *          left      : the left margin of the element in percent of the width of the canvas
     *          top       : the top margin of the element in percent of the height of the canvas
     *          rows      : the number of rows
     */
    init : function (tag, type, wrapperId, fieldId, width, height, left, top, rows) {

        // get parent
        var divScreen = document.getElementById("screen");

        // create nodes
        this.elem    = document.createElement('div');
        this.field   = document.createElement(tag);

        if (tag == 'input') {
            this.field.type = type;
        }
        this.isCodeMirror = false;
        this.isAce = false;

        if (tag == 'div') {
        	this.field.contentEditable = true;

        	if (this.field.innerText) {
        		this.field.innerText = '';
        	}
        	else {
        		this.field.textContent = '\u0000'; // workaround for Firefox bug
        	}

        }
        else {
        	this.field.value = '';
        }

        this.elem.id             = wrapperId;
        this.field.id            = fieldId;
        this.elem.style.position = 'absolute';

        if (tag == 'div') {
        	this.field.style.overflow = 'scroll';
        }
        else {
        	this.field.style.overflow = 'scroll';
        }
        
        this.field.style.width = '100%';
        this.field.style.height = '100%';
        this.field.style.paddingRight = '17px';
        this.field.style.paddingBottom = '17px';
        this.field.style.outlineColor = 'transparent'; // for the strange behaviour of Chrome
        this.field.style.whiteSpace = 'pre-wrap';
        
        // scale element
        this.relWidth  = width;
        this.relHeight = height;
        this.relLeft   = left;
        this.relTop    = top;
        this.rows      = rows;
        game.scaleElement(this);

        // visibility
        this.visibility = true;
        
        // tag
        this.tag = tag;

        // append nodes

        this.elem.appendChild(this.field);
        divScreen.appendChild(this.elem);
        
        if (tag == 'textarea' ) {
        	if (type == 'codemirror') {
        		this.editor = CodeMirror.fromTextArea(this.field, {
                    mode: "text/x-sql",
                    lineNumbers: true,
                    matchBrackets: true,
                    inputStyle: "contenteditable",
                    scrollbarStyle: "null"
                });
                this.isCodeMirror = true;
            }
            else {
                this.field.name = type;
            }
        } else if (tag == 'pre') {
            if (type == 'ace') {
                this.editor = ace.edit(this.field.id);
                this.editor.setTheme("ace/theme/ambiance");
                this.editor.getSession().setMode("ace/mode/sql");
                this.isAce = true;

                //remove a lil weird bug with Ace being our configured <pre></pre>
                this.field.style.overflow = '';

                //give ace a fixxed <div></div>
                var knot = document.createElement('div');
                knot.style.position = 'absolute';
                this.field.appendChild(knot);
            }
        }

    },

    /**
     * selector
     * get the node
     */
    getNode : function () {

        return this.field;

    },

    /**
     * method to change the type of the element
     * @param : newType : a string
     */
    changeType : function (newType) {

    	if (this.tag == 'input') {
            this.field.type = newType;
        }

    },

    /**
     * method displays the element
     */
    display : function () {

        this.elem.style.display = "block";
        this.visibility = true;

    },

    /**
     * method hides the element
     */
    hide : function () {

        this.elem.style.display = "none";
        this.visibility = false;

    },

    /**
     * method inserts text into the element
     * @param : text : a string
     */
    insertText : function (text) {
    	
    	if (this.tag == 'div') {
    		if (this.field.innerText) {
    			this.field.innerText = this.field.innerText + text;
    		}
    		else {
    			this.field.textContent = this.field.textContent + text;
    		}
    	}
    	else if (this.isCodeMirror) {
    		this.editor.setValue(text);
    	}
    	else {
            this.field.value = this.field.value + text;
        }

    },
    
    /**
     * method inserts html into the element
     * @param : text : a string
     */
    insertTextAsHTML : function (text) {
    	
    	if (!this.isCodeMirror) {
    	    this.field.innerHTML = text;
    	}
       
    },

    /**
     * method removes the text input
     */
    clear : function () {
        if (this.tag == 'div') {
        	if (this.field.innerText) {
        		this.field.innerText = '';
        	}
        	else {
        		this.field.innerHTML = '&nbsp;'; // set magic character
        	}
        }
        else if (this.isCodeMirror) {
        	this.editor.setValue('');
        }
        else {
        	this.field.value = '';
        }
    },

    /**
     * method to get the text
     */
    getText : function () {
    	var text;
    	if (this.tag == 'div') {
    		if (this.field.innerText) {
    			// all browsers except firefox
    			this.field.innerHTML = this.field.innerHTML.replace(/<section[^]*?inline">/g, '');
    			this.field.innerHTML = this.field.innerHTML.replace(/<.section>/g, '');
    			text = this.field.innerText;
    		}
    		else {
                // firefox (with bug handler for bug 641239)
                // see: https://bugzilla.mozilla.org/show_bug.cgi?id=641239
                //
    			// delete the magic char
    			var textWithoutMagicChar = this.field.innerHTML.replace(/&nbsp;/g, ' ');
    			// delete second <br>
    			var regexp1 = new RegExp('<p> *<br> *<\\/p>', 'g');
    			var cleanedText1 = textWithoutMagicChar.replace(regexp1, '');
    			// delete first extra whitespace
    			var regexp2 = new RegExp(' <\\/p>', 'g');
    			var cleanedText2 = cleanedText1.replace(regexp2, '');
    			// replace remaining <br> with a linebreak
    			var textWithLineBreaks = cleanedText2.replace(/<br>/gi, '\n');
    			// delete all remaining html tags
    			var textWithoutTags = textWithLineBreaks.replace(/(<([^>]+)>)/ig, '');
    			// make sure that the magic char is always at the end of the text
    			var textWithMagicChar = textWithoutTags + '\u00a0';
    			// delete second extra whitespace
    			text = textWithMagicChar.replace(/\u0020+\u00a0/, '\u00a0');
    			
    		}	
    	}
    	else if (this.isCodeMirror) {
    		text = this.editor.getValue();
    	}
    	else if (this.isAce) {
            //console.log(this.editor.getValue());
            text = this.editor.getValue();
        } 
        else {
    		text = this.value;
    	}
        return text;
    },

    /**
     * method to add an event to the element
     * @param : event    : a string
     *          callback : a function
     */
    addEvent : function (event, callback) {

        myAddEvent(this.field, event, callback);

    },

    /**
     * method to remove an event from the element
     * @param : event    : a string
     *          callback : a function
     */
    removeEvent : function (event, callback) {

        myRemoveEvent(this.field, event, callback);

    },

    update : function () {

        // scale element
        game.scaleElement(this);

    },

    destroy : function () {

        // remove child
        this.elem.parentNode.removeChild(this.elem);
    }


});

/**
 * defines a scaleable object for text output
 * @author: Christian Reineke
 */
game.TextOutputElement = me.Renderable.extend ({

    /**
     * constructor
     * @param : id     : a unique alphanumeric string
     *          width  : the width of the element in percent of the width of the canvas
     *          height : the height of the element in percent of the height of the canvas
     *          left   : the left margin of the element in percent of the width of the canvas
     *          top    : the top margin of the element in percent of the height of the canvas
     *          rows   : the number of rows
     */
    init : function (id, width, height, left, top, rows) {

        // get parent
        var div = document.getElementById("screen");

        // create nodes
        this.elem                = document.createElement("div");
        this.wrapper             = document.createElement("div");
        this.elem.id             = id;
        this.elem.style.position = 'absolute';
        
        this.wrapper.style.width = '100%';
        this.wrapper.style.height = '100%';
        this.wrapper.style.overflow = 'scroll';
        this.wrapper.style.paddingRight = '17px';
        this.wrapper.style.paddingBottom = '17px';
        
        // scale element
        this.relWidth  = width;
        this.relHeight = height;
        this.relLeft   = left;
        this.relTop    = top;
        this.rows      = rows;
        game.scaleElement(this);

        // visibility
        this.visibility = true;

        // append nodes
        this.elem.appendChild(this.wrapper);
        div.appendChild(this.elem);

    },

    /**
     * selector
     * get the node
     */
    getNode : function () {

        return this.elem;

    },

    update : function () {

        // scale element
        game.scaleElement(this);

    },

    /**
     * method removes all children of the element
     */
    clear : function () {

        while (this.wrapper.hasChildNodes()) {
            this.wrapper.removeChild(this.wrapper.firstChild);
        }

    },

    /**
     * method displays the element
     */
    display : function () {

        this.elem.style.display = "block";
        this.visibility = true;

    },

    /**
     * method hides the element
     */
    hide : function () {

        this.elem.style.display = "none";
        this.visibility = false;

    },

    /**
     * method inserts a string as 'span' into the element
     * @param : text : inserted string
     *          id   : a unique alphanumeric string
     */
    write : function (text, id) {

        var textNode = document.createTextNode(text);
        var spanElem = document.createElement('span');
        spanElem.id  = id;
        spanElem.appendChild(textNode);
        this.wrapper.appendChild(spanElem);

    },

    /**
     * method inserts html code into the element
     * @param : html : inserted html code
     *          id   : a unique alphanumeric string
     */
    writeHTML : function (html, id) {

        var divHTML = document.createElement("div");
        divHTML.id  = id;
        divHTML.insertAdjacentHTML('beforeend', html);
        this.wrapper.appendChild(divHTML);

    },

    /**
     * method inserts a string as 'p' into the element
     * @param : text : inserted string
     *          id   : a unique alphanumeric string
     */
    writePara : function (text, id) {

        var textNode = document.createTextNode(text);
        if (document.getElementById(id) != null){
            var br = document.createElement("br");
            document.getElementById(id).appendChild(br);
            document.getElementById(id).appendChild(textNode);
        } else {
        var paraElem = document.createElement('p');
        paraElem.id  = id;
        paraElem.appendChild(textNode);
        this.wrapper.appendChild(paraElem);
        }
    },

    destroy : function () {

        // remove element
        this.elem.parentNode.removeChild(this.elem);

    }

});

/**
 * defines a clickable object
 * @author: Christian Reineke
 */
game.ClickableElement = me.Renderable.extend ({

    /**
     * constructor
     * @param : id       : a unique alphanumeric string
     *          name     : text to display on screen
     *          callback : the callback function
     *          width    : the width of the element in percent of the width of the canvas
     *          height   : the height of the element in percent of the height of the canvas
     *          left     : the left margin of the element in percent of the width of the canvas
     *          top      : the top margin of the element in percent of the height of the canvas
     *          rows     : the number of rows
     */
    init : function (id, name, callback, width, height, left, top, rows) {

        // get parent
        var parent = document.getElementById('screen');

        // create child
        this.elem                = document.createElement('div');
        this.elem.id             = id;
        this.elem.style.position = 'absolute';

        // scale element
        this.relWidth  = width;
        this.relHeight = height;
        this.relLeft   = left;
        this.relTop    = top;
        this.rows      = rows;
        game.scaleElement(this);

        // visibility
        this.visibility = true;

        // create text node
        var text = document.createTextNode(name);
       
        // append text node
        this.elem.appendChild(text);

        // append child
        parent.appendChild(this.elem);
        
        // make element clickable
        myAddEvent(this.elem, 'click', callback);

    },

    /**
     * selector
     * get the node
     */
    getNode : function () {

        return this.elem;

    },

    /**
     * method displays the element
     */
    display : function () {

        this.elem.style.display = "block";
        this.visibility = true;

    },

    /**
     * method hides the element
     */
    hide : function () {

        this.elem.style.display = "none";
        this.visibility = false;

    },
    
    setImage : function (src, id) {
    	
    	var image = document.createElement("img");
    	image.src = src;
    	image.id = id;
    	image.style.height = 'inherit';
    	image.style.width = 'inherit';
    	this.elem.appendChild(image);
    	
    },

    update : function () {

        // scale element
        game.scaleElement(this);


    },

    destroy : function () {

        // remove element
        this.elem.parentNode.removeChild(this.elem);

    },

    changeColor : function (id) {
        //change id from button (for new style (color))
        this.elem.id = id;
    }

});