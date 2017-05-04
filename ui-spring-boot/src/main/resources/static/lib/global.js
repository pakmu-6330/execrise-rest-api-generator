function isStringEmpty(s) {
    if (!s) {
        return true;
    } else {
        if (s.replace(/(^s*)|(s*$)/g, "").length == 0) {
            return true;
        } else {
            return false;
        }
    }
}

String.prototype.trim = function () {
    return this.replace(/(^\s*)|(\s*$)/g, "");
}

function showSuccessToast(content) {
    $().toastmessage('showToast', {
        stayTime: 3000,
        text: content,
        sticky: false,
        position: 'top-center',
        type: 'success'
    });
}

function showErrorToast(content) {
    $().toastmessage('showToast', {
        stayTime: 3000,
        text: content,
        sticky: false,
        position: 'top-center',
        type: 'error'
    });
}