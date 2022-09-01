import { useState } from "react";

import styled from 'styled-components'

const FormInput = (props) => {
  const [focused, setFocused] = useState(false);
  const { label, errorMessage, onChange, display, icon, id, ...inputProps } = props;

  const handleFocus = (e) => {
    setFocused(true);
  };

  return (
    <div className="formInput">
      {
        display == "true" &&
        <Label className="mt-2 mb-0">{label}</Label>
      }
      
      {
        icon == "true" &&
        <div> 
          <div className="row">
            <div className="col-2">
              {
                id == 1 &&
                <Label htmlFor="email" className="d-flex me-3">
                  <i className="bi bi-envelope-fill" style={{ fontSize: '30px' }} />
                </Label>
                ||
                id == 2 &&
                <Label htmlFor="phone" className="d-flex me-3">
                  <i className="bi bi-telephone-fill" style={{ fontSize: '30px' }} />
                </Label>
              }

            </div>
            <div className="col-10">
              <input
                className="mb-2"
                {...inputProps}
                onChange={onChange}
                onBlur={handleFocus}
                onFocus={() =>
                  inputProps.name === "confirmPassword" && setFocused(true)
                }
                focused={focused.toString()}
              />
              <span>{errorMessage}</span>
            </div>
          </div>
        </div>
        ||
        <div>
          <input
                  className="mb-2"
                  {...inputProps}
                  onChange={onChange}
                  onBlur={handleFocus}
                  onFocus={() =>
                    inputProps.name === "confirmPassword" && setFocused(true)
                  }
                  focused={focused.toString()}
                />
                <span>{errorMessage}</span>

        </div>
      }
    </div>
  );
};

export default FormInput;

const Label = styled.label`
    font-size: 20px;
    color: #035eb8;
    font-weight: bold;
`

const Grid = styled.div`
    display: grid;

    
    grid-template-columns: repeat(2, 1fr)
    
`
