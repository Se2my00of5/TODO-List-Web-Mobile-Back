
function dict(text,flag=0){
	return {"description":text,"is_completed":flag};
}

var arrayToDo = [];

//добавление нового дела
document.getElementById('add-btn').addEventListener('click', function() {
    let inputText = document.getElementById('input-text');
    if (inputText.value.trim() !== '') {
		arrayToDo.push(dict(inputText.value.trim()));

		inputText.value = '';
		showTasks(); 
    }
});

//удаление дела
function deleteTask(index){
	arrayToDo.splice(index, 1); 

	showTasks(); 
}


//редактирование дела
function editTask(index){

	let text = arrayToDo[index].description;
	let liTag = document.getElementById("edit"+index).closest("li");
	
	let editInput = document.createElement('textarea');
	editInput.className = 'edit-input';
	editInput.value = text;

	let saveButton = document.createElement('span');
	saveButton.innerHTML = `<i class="fas fa-save"></i>`;
	saveButton.className = 'save';


	let liTagNew = document.createElement('li');
	liTagNew.appendChild(editInput);
	liTagNew.appendChild(saveButton);

	liTag.replaceWith(liTagNew);

	// сохранение дела
	saveButton.addEventListener('click', function() {
		if (editInput.value.trim() !== '') {
			if(editInput.value.trim()!=text){
				arrayToDo.splice(index, 1, dict(editInput.value.trim())); 
			}
			
			showTasks(); 
		}
	});
}

function acceptOrCancelTask(index){
	arrayToDo[index].is_completed = Math.abs(arrayToDo[index].is_completed-1);
	
	showTasks();
}

//отображение списка дел
function showTasks(){
	let listToDo = document.getElementById('list-todo');
	
	let newLiTag = "";
	arrayToDo.forEach((element, index) => {
		if(element.is_completed==0){//не выполнено
			newLiTag +=	`<li>${element.description}
						<span class="trash" onclick="deleteTask(${index})">
							<i class="fas fa-trash"></i>
						</span>
						<span class="edit" id="edit${index}" onclick="editTask(${index})" >
							<i class="fas fa-edit"></i>
						</span>
						<span class="accept" onclick="acceptOrCancelTask(${index})">
							<i class="fas fa-check"></i>
						</span>
                	</li>`;
		}
		else{//выполнено
			newLiTag +=	`<li style="background: rgb(85, 231, 141);">${element.description}
						<span class="trash" onclick="deleteTask(${index})">
							<i class="fas fa-trash"></i>
						</span>
						<span class="edit" id="edit${index}" onclick="editTask(${index})" >
							<i class="fas fa-edit"></i>
						</span>
						<span class="cancel" onclick="acceptOrCancelTask(${index})">
							<i class="fas fa-window-close"></i>
						</span>
                	</li>`;
		}
	  	
	});

	listToDo.innerHTML = newLiTag;
}



/*document.getElementById('save').addEventListener('click', function() {
    let jsonStr = JSON.stringify(arrayToDo, null, 2);

    let url = "data:text/json;charset=utf-8," + encodeURIComponent(jsonStr);

    let download = document.createElement('a');
    download.href = url;
    download.download = "todolist.json";

    download.click();
});*/

document.getElementById('save').addEventListener('click', function(){
	fetch('http://127.0.0.1:8000/api/tasks/load_tasks/', {
		method: 'POST',
		headers: {
		  'Content-Type': 'application/json',
		  'Accept': 'application/json',
		},
		body: JSON.stringify(arrayToDo),
	  })
	  .then(response => response.json())
	  .then(data => {
		console.log('Успех:', data);
		// Можно обновить список дел на странице
	  })
	  .catch(error => console.error('Ошибка:', error));

});

document.getElementById('load').addEventListener('click', function() {
	fetch('http://127.0.0.1:8000/api/tasks/', {
		method: 'GET',
		headers: {
		  'Accept': 'application/json',
		}
	  })
	  .then(response => response.json())
	  .then(data => {
		console.log(data);

		arrayToDo = [];
		for(index in data){
			console.log(data[index]);
			arrayToDo.push(dict(data[index].description, data[index].is_completed))
		}

		showTasks();
		
	  })
	  .catch(error => console.error('Ошибка:', error));
});
