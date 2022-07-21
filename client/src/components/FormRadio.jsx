import React from 'react'

const FormRadio = (props) => {
    const { id, name, value, label, values, handleClick } = props;

    return (
        <div className="d-flex align-items-center my-1">
            {
                values[name] === value &&
                <input type="radio" id={id} name={name} value={value} onClick={(e) => handleClick(e)} defaultChecked/>
                ||
                <input type="radio" id={id} name={name} value={value} onClick={(e) => handleClick(e)}/>
            }
            <label htmlFor="obstetric" className="ms-2">{label}</label>
        </div>
    );
  };
  
  export default FormRadio;
